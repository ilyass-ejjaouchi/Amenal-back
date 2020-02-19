package org.amenal.metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.dao.OuvrierRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.FicheTypeEnum;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.FicheCommande;
import org.amenal.rest.commande.ProjetCommande;
import org.amenal.rest.mapper.FicheOuvrierMapper;
import org.amenal.rest.mapper.OuvrierMapper;
import org.amenal.rest.mapper.ProjetMapper;
import org.amenal.rest.representation.OuvrierFichePresentation;
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
	OuvrierRepository ouvrierDao;

	@Autowired
	ProjetMapper projetMapper;
	
	@Autowired
	OuvrierMapper ouvrierMapper;

	@Autowired
	FicheOuvrierMapper ficheOuvrierMapper;

	public Projet addProjet(ProjetCommande p_cmd) {
		Projet projet = projetMapper.toEntity(p_cmd);
		List<FicheTypeEnum> fichetype = projet.getFichierTypes();

		if (projetDao.findByTitre(projet.getTitre()) != null)
			throw new BadRequestException("Le projet " + projet.getTitre() + " déjà existant.");

		projet.setFichiers(CreateFiches(fichetype, projet));
		return projetDao.save(projet);
	}
	
	

	public List<ProjetPresentation> ListProjet() {

		return projetDao.findAll().stream().map(o -> projetMapper.toRepresentation(o)).collect(Collectors.toList());

	}

	public void AssocierOuvrierProjet(Integer idOuvrier, Integer idProjet) {

		Optional<Ouvrier> ouvrier = ouvrierDao.findById(idOuvrier);
		Optional<Projet> projet = projetDao.findById(idProjet);
		System.out.println("************************************************"+ouvrier.get().getCin());
		if (!ouvrier.isPresent()) {
			throw new BadRequestException("Cet ouvrier("+idOuvrier+") est introuvable!");
		}
		if (!projet.isPresent()) {

			throw new BadRequestException("Ce projet est introuvable!");
		}
		
		List<Ouvrier> ouvs = projet.get().getOuvriers().stream().filter(ouv->ouv.getId()==idOuvrier).collect(Collectors.toList());
	
		if(ouvs.isEmpty()) 
			projet.get().addOuvrier(ouvrier.get());
		else {
			projet.get().setOuvriers(projet.get().getOuvriers().stream().filter(ouv->  ouv.getId()!=idOuvrier).collect(Collectors.toList()));		
			ouvrier.get().setProjets(ouvrier.get().getProjets().stream().filter(p->  p.getId()!=idProjet).collect(Collectors.toList()));		
		}
			
	}

	public List<OuvrierFichePresentation> GetFicherByProjet(FicheCommande ficheCmd) {


		Integer idProjet = ficheCmd.getIdProjet();
		String type = ficheCmd.getType();
		Date date = ficheCmd.getDate();

		OuvrierFichePresentation fichePrst = new OuvrierFichePresentation();

		Optional<Projet> projet = projetDao.findById(idProjet);

		if (!projet.isPresent())
			throw new BadRequestException("le projet est inexistant!");

		List<FicheTypeEnum> list = projet.get().getFichierTypes().stream()
				.filter((typeEnum) -> typeEnum.getCode().equals(type)).collect(Collectors.toList());

		if (list.isEmpty())
			throw new BadRequestException(
					"Le type [" + type + "] n' existe pas pour le projet [" + projet.get().getTitre() + "]");

		final List<OuvrierFiche> fs = OuvFicheDao.findByProjetAndTypeFicheAndDate(1, type, date);

		List<OuvrierFiche> fiches = fs.stream().map(x -> {
			x.setCount(fs.indexOf(x));
			return x;
		}).collect(Collectors.toList());

		return fiches.stream().map(o -> ficheOuvrierMapper.toRepresentation(o)).collect(Collectors.toList());

	}
	
	public List<OuvrierPresentation> listerOuvrierByProjet(Integer projetID) {
		Optional<Projet> projet = projetDao.findById(projetID);

		if (!projet.isPresent())
			throw new BadRequestException("le projet avec l' id ["+projetID+ "] est inexistant!");

		return projetDao.findByProjetID(projetID).stream().map(o -> ouvrierMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	private static List<Fiche> CreateFiches(List<FicheTypeEnum> ficheTypes, Projet p) {

		List<Fiche> fiches = new ArrayList<Fiche>();

		ficheTypes.forEach(type -> {
			switch (type) {
			case LOC:

				break;
			case MOO:
				OuvrierFiche OuvFiche = new OuvrierFiche();
				OuvFiche.setDate(new Date());
				OuvFiche.setProjet(p);

				fiches.add(OuvFiche);
				break;

			default:
				break;
			}
		});

		return fiches;

	}

}
