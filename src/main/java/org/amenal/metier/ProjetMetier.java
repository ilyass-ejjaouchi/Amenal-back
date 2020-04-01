package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.LocationFicheRepository;
import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.dao.OuvrierRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.dao.StockFicheRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.FicheTypeEnum;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.FicheCommande;
import org.amenal.rest.commande.ProjetCommande;
import org.amenal.rest.mapper.FicheLocationMapper;
import org.amenal.rest.mapper.FicheOuvrierMapper;
import org.amenal.rest.mapper.FicheReceptionMapper;
import org.amenal.rest.mapper.LocationDesignationMapper;
import org.amenal.rest.mapper.OuvrierMapper;
import org.amenal.rest.mapper.ProjetMapper;
import org.amenal.rest.representation.FichePresentation;
import org.amenal.rest.representation.OuvrierPresentation;
import org.amenal.rest.representation.ProjetPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjetMetier {

	@Autowired
	ProjetRepository projetDao;

	@Autowired
	OuvrierFicheRepository OuvFicheDao;

	@Autowired
	LocationFicheRepository locationFicheRepository;

	@Autowired
	ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	OuvrierRepository ouvrierDao;

	@Autowired
	OuvrierDesignationRepository OuvrierDesignationDao;

	@Autowired
	ProjetMapper projetMapper;

	@Autowired
	OuvrierMapper ouvrierMapper;
	
	@Autowired 
	StockMetier stockMetier;
	
	@Autowired
	StockFicheRepository stockFicheRepository;

	@Autowired
	FicheOuvrierMapper ficheOuvrierMapper;

	@Autowired
	FicheLocationMapper ficheLocationMapper;

	@Autowired
	FicheReceptionMapper ficheReceptionMapper;

	private static Boolean createStck;

	public Projet addProjet(ProjetCommande p_cmd) {
		Projet projet = projetMapper.toEntity(p_cmd);
		List<FicheTypeEnum> fichetype = projet.getFichierTypes();

		if (projetDao.findByTitre(projet.getTitre()) != null)
			throw new BadRequestException("Le projet " + projet.getTitre() + " déjà existant.");

		projet.setFichiers(CreateFiches(fichetype, projet));
		return projetDao.save(projet);

	}

	public Projet modifierProjet(ProjetCommande p_cmd, Integer id) {
		Projet projet = projetMapper.toEntity(p_cmd);
		List<FicheTypeEnum> fichetypes = projet.getFichierTypes();

		Projet p = projetDao.findByTitre(projet.getTitre());

		if (p == null)
			throw new BadRequestException("Le projet " + projet.getTitre() + " n'est pas existant.");

		projet.setId(id);
		projet.setFichiers(CreateFiches(fichetypes, projet));
		fichetypes.addAll(p.getFichierTypes());
		projet.setFichierTypes(fichetypes);
		return projetDao.save(projet);

	}

	public List<ProjetPresentation> ListProjet() {

		return projetDao.findAll().stream().map(o -> projetMapper.toRepresentation(o)).collect(Collectors.toList());

	}

	public void AssocierOuvrierProjet(Integer idOuvrier, Integer idProjet) {

		Optional<Ouvrier> ouvrier = ouvrierDao.findById(idOuvrier);
		Optional<Projet> projet = projetDao.findById(idProjet);
		if (!ouvrier.isPresent()) {
			throw new BadRequestException("Cet ouvrier(" + idOuvrier + ") est introuvable!");
		}
		if (!projet.isPresent()) {

			throw new BadRequestException("Ce projet est introuvable!");
		}

		List<Ouvrier> ouvs = projet.get().getOuvriers().stream().filter(ouv -> ouv.getId() == idOuvrier)
				.collect(Collectors.toList());

		if (ouvs.isEmpty())
			projet.get().addOuvrier(ouvrier.get());
		else {
			projet.get().setOuvriers(projet.get().getOuvriers().stream().filter(ouv -> ouv.getId() != idOuvrier)
					.collect(Collectors.toList()));
			ouvrier.get().setProjets(ouvrier.get().getProjets().stream().filter(p -> p.getId() != idProjet)
					.collect(Collectors.toList()));
		}

	}

	public List<FichePresentation> GetFicherByProjet(FicheCommande ficheCmd) {

		Integer idProjet = ficheCmd.getIdProjet();
		String type = ficheCmd.getType();
		LocalDate date = ficheCmd.getDate();

		FichePresentation fichePrst = new FichePresentation();

		Optional<Projet> projet = projetDao.findById(idProjet);

		if (!projet.isPresent())
			throw new BadRequestException("le projet est inexistant!");

		List<FicheTypeEnum> list = projet.get().getFichierTypes().stream()
				.filter((typeEnum) -> typeEnum.getCode().equals(type)).collect(Collectors.toList());

		if (list.isEmpty())
			throw new BadRequestException(
					"Le type [" + type + "] n' existe pas pour le projet [" + projet.get().getTitre() + "]");

		switch (type) {
		case "OUVRIER": {
			List<OuvrierFiche> fs = OuvFicheDao.findByProjetAndTypeFicheAndDate(idProjet, type, date);
			List<OuvrierFiche> fiches = fs.stream().map(x -> {
				x.setCount(fs.indexOf(x));

				return x;
			}).collect(Collectors.toList());

			return fiches.stream().map(o -> ficheOuvrierMapper.toRepresentation(o)).collect(Collectors.toList());
		}
		case "LOCATION": {
			List<LocationFiche> fs = locationFicheRepository.findByProjetAndTypeFicheAndDate(idProjet, type, date);
			List<LocationFiche> fiches = fs.stream().map(x -> {
				x.setCount(fs.indexOf(x));
				if (!x.getIsValidated()) {
					x.getLocationDesignations().forEach(ds -> {
						Article ar = ds.getMateriel();
						Fournisseur fr = ds.getFournisseur();
						ds.setLibelle(ar.getDesignation());
						ds.setUnite(ds.getUnite());
						ds.setFournisseurNom(fr.getFournisseurNom());
					});
				}
				return x;
			}).collect(Collectors.toList());

			return fiches.stream().map(o -> ficheLocationMapper.toRepresentation(o)).collect(Collectors.toList());
		}
		case "RECEPTION": {
			List<ReceptionFiche> fs = receptionFicheRepository.findByProjetAndTypeFicheAndDate(idProjet, type, date);

			return fs.stream().map(o -> ficheReceptionMapper.toRepresentation(o)).collect(Collectors.toList());
		}
		case "STOCK": {

			List<StockFiche> fs = stockFicheRepository.findByProjetAndTypeFicheAndDate(idProjet, type, date);
			
			List<FichePresentation> fsPrs = fs.stream().map(o -> {
				FichePresentation ff=  new FichePresentation();
				ff.setId(o.getId());
				ff.setDate(o.getDate());
				ff.setStockDesignations(stockMetier.getStockLigneDesignation(idProjet, ff.getDate()));
				return ff;
			}).collect(Collectors.toList());
			;
			
			return fsPrs;

		}
		default:
			break;
		}

		return null;

	}

	public List<OuvrierPresentation> listerOuvrierByProjet(Integer projetID, Integer ficheID) {
		Optional<Projet> projet = projetDao.findById(projetID);

		if (!projet.isPresent())
			throw new BadRequestException("le projet avec l' id [" + projetID + "] est inexistant!");

		List<Ouvrier> ouvriers = projetDao.findOuvrierByProjetID(projetID);
		ouvriers.removeAll(OuvrierDesignationDao.findOuvrierByFiche(ficheID));

		return ouvriers.stream().map(o -> ouvrierMapper.toRepresentation(o)).collect(Collectors.toList());

	}

	private static List<Fiche> CreateFiches(List<FicheTypeEnum> ficheTypes, Projet p) {

		List<Fiche> fiches = new ArrayList<Fiche>();

		createStck = false;

		ficheTypes.forEach(type -> {
			switch (type) {
			case MOO:
				OuvrierFiche OuvFiche = new OuvrierFiche();
				OuvFiche.setDate(LocalDate.now());
				OuvFiche.setProjet(p);
				fiches.add(OuvFiche);
				createStck = true;
				break;
			case LOC:
				LocationFiche locFiche = new LocationFiche();
				locFiche.setDate(LocalDate.now());
				locFiche.setProjet(p);
				fiches.add(locFiche);
				createStck = true;

			case RCP:
				ReceptionFiche recf = new ReceptionFiche();
				recf.setDate(LocalDate.now());
				recf.setProjet(p);
				fiches.add(recf);
				createStck = true;
			default:
				break;
			}
		});
		if (createStck) {
			StockFiche stockFiche = new StockFiche();
			stockFiche.setDate(LocalDate.now());
			stockFiche.setProjet(p);
			fiches.add(stockFiche);
		}

		return fiches;

	}

}
