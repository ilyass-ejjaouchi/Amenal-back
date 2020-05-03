package org.amenal.metier;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.amenal.dao.AccidentFicherepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.FicheBesoinRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.VisiteurDesignationRepository;
import org.amenal.dao.VisiteurFicheRepository;
import org.amenal.dao.VisiteurRepository;
import org.amenal.entities.Document;
import org.amenal.entities.Projet;
import org.amenal.entities.Visiteur;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.entities.designations.VisiteurDesignation;
import org.amenal.entities.fiches.AccidentFiche;
import org.amenal.entities.fiches.BesoinFiche;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.entities.fiches.VisiteurFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.VisiteurCommande;
import org.amenal.rest.commande.VisiteurDesignationCommande;
import org.amenal.rest.mapper.VisiteurDesignationMapper;
import org.amenal.rest.mapper.VisiteurMapper;
import org.amenal.rest.representation.VisiteurPresentation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class FicheVisiteurMetier {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	VisiteurRepository visiteurRepository;

	@Autowired
	VisiteurFicheRepository visiteurFicheRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	VisiteurMapper visiteurMapper;

	@Autowired
	VisiteurDesignationMapper visiteurDesignationMapper;

	@Autowired
	VisiteurDesignationRepository visiteurDesignationRepository;

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	AccidentFicherepository accidentFicherepository;

	@Autowired
	FicheBesoinRepository ficheBesoinRepository;

	@Autowired
	DocFicheRepository docFicheRepository;

	@Autowired
	StockMetier stockMetier;

	public void emportExcelFile(MultipartFile excelFile) throws EncryptedDocumentException, InvalidFormatException {

		Workbook workbook;
		try {
			new WorkbookFactory();
			workbook = WorkbookFactory.create(excelFile.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BadRequestException("format de fichier non supporter!");
		}
		Sheet dataSheet = workbook.getSheetAt(0);

		int i = 1;
		for (Row row : dataSheet) {
			if (i > 1) {
				Visiteur vst = new Visiteur();

				if (row.getCell(0).getCellTypeEnum() == CellType.STRING)
					vst.setNom(row.getCell(0).getStringCellValue());
				else
					throw new BadRequestException(
							"la colonne INTITULE  doit etre en format chacractaire (" + i + ",1)");
				if (row.getCell(1).getCellTypeEnum() == CellType.STRING)
					vst.setNom(row.getCell(1).getStringCellValue());
				else
					throw new BadRequestException(
							"la colonne ORGANISEME doit etre en format chacractaire (" + i + ",1)");

				if (visiteurRepository.findByNomAndOrganime(vst.getNom(), vst.getOrganisme()) == null)
					visiteurRepository.save(vst);
			}
			i++;
		}
	}

	public void addVisiteurDesignation(VisiteurDesignationCommande vstCmd) {

		VisiteurDesignation ds = visiteurDesignationMapper.toEntity(vstCmd);
		Optional<VisiteurFiche> ff = visiteurFicheRepository.findById(vstCmd.getIdFiche());

		if (!ff.isPresent())
			throw new BadRequestException("la fiche est inrouvable");

		ds.setFiche(ff.get());
		if (vstCmd.getVisiteurId() != null) {
			Optional<Visiteur> vs = visiteurRepository.findById(vstCmd.getVisiteurId());
			if (!vs.isPresent())
				throw new BadRequestException("le visiteur est inrouvable");
			ds.setVisiteur(vs.get());
		}

		visiteurDesignationRepository.save(ds);

	}

	public void updateVisiteurDesignation(VisiteurDesignationCommande vstCmd, Integer id) {

		VisiteurDesignation ds = visiteurDesignationMapper.toEntity(vstCmd);
		Optional<VisiteurFiche> ff = visiteurFicheRepository.findById(vstCmd.getIdFiche());

		if (!ff.isPresent())
			throw new BadRequestException("la fiche est introuvable");

		ds.setFiche(ff.get());
		if (ds.getVisiteur() != null) {
			Optional<Visiteur> vs = visiteurRepository.findById(vstCmd.getVisiteurId());
			if (!vs.isPresent())
				throw new BadRequestException("le visiteur est introuvable");
			ds.setVisiteur(vs.get());
		}

		ds.setId(id);

		visiteurDesignationRepository.save(ds);

	}

	public void deleteVisiteurDesignation(Integer id) {
		this.visiteurDesignationRepository.deleteById(id);
	}

	public void AssosierVisiteurToProjet(Integer pid, Integer vid) {

		Optional<Visiteur> v = visiteurRepository.findById(vid);
		Optional<Projet> p = projetRepository.findById(pid);

		if (v.get().getProjets().contains(p.get()))
			p.get().getVisiteurs().remove(v.get());
		else
			p.get().getVisiteurs().add(v.get());

	}

	public List<VisiteurPresentation> listerVisiteur(Integer pid) {

		return visiteurRepository.findAll().stream().map(o -> {

			VisiteurPresentation vp = visiteurMapper.toRepresentation(o);
			vp.setIsAsso(false);
			o.getProjets().forEach(p -> {
				if (pid == p.getId()) {
					vp.setIsAsso(true);
				}
			});
			return vp;
		}).collect(Collectors.toList());

	}

	public void addVisteur(VisiteurCommande vsCmd) {

		Visiteur vs = visiteurMapper.toEntity(vsCmd);
		visiteurRepository.save(vs);
	}

	public void updateVisteur(VisiteurCommande vsCmd, Integer id) {

		Visiteur vs = visiteurMapper.toEntity(vsCmd);
		vs.setId(id);
		visiteurRepository.save(vs);
	}

	public void deleteVisteur(Integer id) {

		Optional<Visiteur> v = visiteurRepository.findById(id);

		if (!v.get().getProjets().isEmpty())
			throw new BadRequestException("On peut pas supprimer ce visiteur , il est associer a des projets!");

		visiteurRepository.deleteById(id);
	}

	public List<VisiteurPresentation> getListVisiteur(Integer pid) {
		return this.visiteurRepository.findByProjetId(pid).stream().map(o -> visiteurMapper.toRepresentation(o))
				.collect(Collectors.toList());
	}

	public void validerVisiteur(Integer id) {

		Optional<VisiteurFiche> fiche = visiteurFicheRepository.findById(id);
		Projet p = fiche.get().getProjet();

		if (!fiche.get().getIsValidated()) {

			for (VisiteurDesignation d : fiche.get().getVisiteurDesignations()) {
				if (d.getDepart() == null)
					throw new BadRequestException("Vous devez completer la fiche!");
			}

			LivraisonFiche livF = livraisonFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			AccidentFiche accF = accidentFicherepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());

			BesoinFiche bsnFiche = ficheBesoinRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			DocFiche docFiche = docFicheRepository.findByDateAndProjet(fiche.get().getDate(), fiche.get().getProjet());

			fiche.get().setIsValidated(true);

			if (livF.getIsValidated() && accF.getIsValidated() && docFiche.getIsValidated()
					&& bsnFiche.getIsValidated()) {

				List<Fiche> fiches = new ArrayList<Fiche>();

				LocalDate date = fiche.get().getDate().plusDays(1);
				stockMetier.validerFicheStock(fiche.get().getDate(), fiche.get().getProjet());

				/* OUVRIER */
				OuvrierFiche ouvFiche = new OuvrierFiche();
				ouvFiche.setProjet(p);
				ouvFiche.setDate(date);
				fiches.add(ouvFiche);

				/* LOCATION */
				LocationFiche locFiche = new LocationFiche();
				locFiche.setDate(date);
				locFiche.setProjet(p);
				fiches.add(locFiche);
				/* RECEPTION */
				ReceptionFiche recf = new ReceptionFiche();
				recf.setDate(date);
				recf.setProjet(p);
				fiches.add(recf);
				/* LIVRAISON */
				LivraisonFiche liv = new LivraisonFiche();
				liv.setDate(date);
				liv.setProjet(p);
				fiches.add(liv);
				/* DOCUMENT */
				DocFiche dic = new DocFiche();
				p.getDocuments().forEach(doc -> {
					DocDesignation ds = new DocDesignation();
					ds.setDocument(doc);
					ds.setIntitule(doc.getIntitule());
					ds.setDisponibilite(false);
					ds.setFiche(dic);
					dic.getDocDesignations().add(ds);
				});

				dic.setDate(date);
				dic.setProjet(p);
				fiches.add(dic);
				/* STOCK */
				StockFiche stockFiche = new StockFiche();
				stockFiche.setDate(date);
				stockFiche.setProjet(p);
				fiches.add(stockFiche);
				/* ACCIDENT */
				AccidentFiche accFiche = new AccidentFiche();
				accFiche.setDate(date);
				stockFiche.setProjet(p);
				fiches.add(accFiche);
				/* BESOIN */
				BesoinFiche bsnnFiche = new BesoinFiche();
				bsnnFiche.setDate(date);
				bsnnFiche.setProjet(p);
				fiches.add(bsnnFiche);
				/* VISITEUR */
				VisiteurFiche vstFiche = new VisiteurFiche();
				vstFiche.setDate(date);
				vstFiche.setProjet(p);
				fiches.add(vstFiche);

				entityManager.detach(p);

				p.setFichiers(fiches);
				projetRepository.save(p);

			}
		}
	}

}
