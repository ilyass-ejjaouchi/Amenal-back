package org.amenal.metier;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.amenal.dao.AccidentFicherepository;
import org.amenal.dao.DocDesignationRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.DocumentRepository;
import org.amenal.dao.FicheBesoinRepository;
import org.amenal.dao.FicheRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.dao.VisiteurFicheRepository;
import org.amenal.entities.Destination;
import org.amenal.entities.Document;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.DocDesignation;
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
import org.amenal.rest.mapper.DocumentMapper;
import org.amenal.rest.representation.DocumentRepresentation;
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
public class DocumentFicheMetier {
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	FicheBesoinRepository ficheBesoinRepository;
	@Autowired
	VisiteurFicheRepository visiteurFicheRepository;

	@Autowired
	private DocFicheRepository docFicheRepository;

	@Autowired
	private DocDesignationRepository docDesignationRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	StockMetier stockMetier;

	@Autowired
	private ProjetRepository projetRepository;

	@Autowired
	private DocumentMapper documentMapper;

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	AccidentFicherepository accidentFicherepository;

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
			if (i > 1 && documentRepository.findByIntitule(row.getCell(0).getStringCellValue().toUpperCase()) == null) {
				Document doc = new Document();

				if (row.getCell(0).getCellTypeEnum() == CellType.STRING)
					doc.setIntitule(row.getCell(0).getStringCellValue());
				else
					throw new BadRequestException(
							"la colonne INTITULE  dois etre en format chacractaire (" + i + ",1)");

				documentRepository.save(doc);
			}
			i++;
		}
	}

	public void AddDocument(String intitule) {
		Document doc = documentRepository.findByIntitule(intitule.trim().toUpperCase());

		if (doc != null)
			throw new BadRequestException("Ce document est deja existant!");

		doc = new Document();
		doc.setIntitule(intitule);

		documentRepository.save(doc);
	}

	public void UpdateDocument(String intitule, Integer id) {
		Document doc = documentRepository.findByIntitule(intitule.trim().toUpperCase());

		if (doc != null && doc.getId() != id)
			throw new BadRequestException("Ce document est deja existant!");
		Optional<Document> docc = documentRepository.findById(id);

		docc.get().setIntitule(intitule);
		docc.get().getDocDesignations().forEach(l -> {
			if (!l.getFiche().getIsValidated()) {
				l.setIntitule(docc.get().getIntitule());
				docDesignationRepository.save(l);
			}
		});

	}

	public void DeleteDocument(Integer id) {

		Optional<Document> doc = documentRepository.findById(id);

		if (!doc.isPresent())
			throw new BadRequestException("Ce document est inexistant!");

		if (doc.get().getProjets().isEmpty())
			documentRepository.deleteById(id);
		else {
			String ps = "";
			for (Projet p : doc.get().getProjets())
				ps += p.getAbreveation() + ",";

			throw new BadRequestException("Ce document ne peut pas etre supprimer il est associer au projet : " + ps);
		}

	}

	public List<DocumentRepresentation> ListerDocument(Integer projetId) {

		return documentRepository.findAll().stream().map(l -> {
			List<Integer> pids = l.getProjets().stream().map(p -> p.getId()).collect(Collectors.toList());
			if (pids.contains(projetId))
				l.setIsAsso(true);
			else
				l.setIsAsso(false);
			return documentMapper.toRepresentation(l);
		}).collect(Collectors.toList());
	}

	public void AssoDocumentToProjet(Integer pid, Integer did) {
		Optional<Document> doc = documentRepository.findById(did);

		if (!doc.isPresent())
			throw new BadRequestException("Ce document est inexistant!");

		Optional<Projet> projet = projetRepository.findById(pid);

		if (!projet.isPresent())
			throw new BadRequestException("Ce projet est inexistant!");

		if (projet.get().getDocuments().contains(doc.get())) {

			projet.get().getDocuments().remove(doc.get());

			DocDesignation ds = docDesignationRepository.findByDocument(doc.get());
			if (ds != null)
				docDesignationRepository.delete(ds);

		} else {
			projet.get().getDocuments().add(doc.get());

			DocDesignation ds = new DocDesignation();
			ds.setDocument(doc.get());
			ds.setIntitule(doc.get().getIntitule());
			ds.setDisponibilite(false);

			docDesignationRepository.save(ds);

			DocFiche f = docFicheRepository.findLastDocFicheByProjet(projet.get());

			ds.setFiche(f);

			f.getDocDesignations().add(ds);
		}
	}

	public void updateDocumentDesignationDisponibilite(Integer id) {
		Optional<DocDesignation> ds = docDesignationRepository.findById(id);

		if (!ds.isPresent())
			throw new BadRequestException("Ceette designation est inexistant!");
		else if (ds.get().getFiche().getIsValidated())
			throw new BadRequestException("Cette fiche est deja validé!");

		if (ds.get().getDisponibilite())
			ds.get().setDisponibilite(false);
		else
			ds.get().setDisponibilite(true);

	}

	public void validerFichedocumet(Integer id) {

		Optional<DocFiche> fiche = docFicheRepository.findById(id);
		Projet p = fiche.get().getProjet();

		if (!fiche.get().getIsValidated()) {

			for (DocDesignation d : fiche.get().getDocDesignations()) {
				if (!d.getDisponibilite())
					throw new BadRequestException("Vous devez completer la fiche!");
			}

			LivraisonFiche livF = livraisonFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			AccidentFiche accF = accidentFicherepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			VisiteurFiche ficheV = visiteurFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			BesoinFiche bsnFiche = ficheBesoinRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());

			fiche.get().setIsValidated(true);

			if (livF.getIsValidated() && accF.getIsValidated() && ficheV.getIsValidated()
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
