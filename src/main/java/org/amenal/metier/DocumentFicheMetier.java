package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.DocDesignationRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.DocumentRepository;
import org.amenal.dao.FicheRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.entities.Document;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.mapper.DocumentMapper;
import org.amenal.rest.representation.DocumentRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentFicheMetier {

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
	private ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

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
			if (!l.getDocFiche().getIsValidated()) {
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

			ds.setDocFiche(f);

			f.getDocDesignations().add(ds);
		}
	}

	public void updateDocumentDesignationDipo(Integer id) {
		Optional<DocDesignation> ds = docDesignationRepository.findById(id);

		if (!ds.isPresent())
			throw new BadRequestException("Ceette designation est inexistant!");
		else if (ds.get().getDocFiche().getIsValidated())
			throw new BadRequestException("Cette fiche est deja validé!");

		if (ds.get().getDisponibilite())
			ds.get().setDisponibilite(false);
		else
			ds.get().setDisponibilite(true);

	}

	public void validerFichedocumet(Integer id) {

		Optional<DocFiche> fiche = docFicheRepository.findById(id);
		Projet p = fiche.get().getProjet();

		for (DocDesignation d : fiche.get().getDocDesignations()) {
			if (!d.getDisponibilite())
				throw new BadRequestException("Vous devez completer la fiche!");
		}

		ReceptionFiche ff = receptionFicheRepository.findByDate(fiche.get().getDate());
		LivraisonFiche livF = livraisonFicheRepository.findByDate(fiche.get().getDate());

		fiche.get().setIsValidated(true);

		if (ff.getIsValidated() && livF.getIsValidated()) {

			List<Fiche> fiches = new ArrayList<Fiche>();

			if (fiche.get().getDate().isBefore(LocalDate.now())) {
				LocalDate date = fiche.get().getDate().plusDays(1);
				stockMetier.validerFicheStock(fiche.get().getDate());

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
					ds.setDocFiche(dic);
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

				p.getFichiers().addAll(fiches);
				projetRepository.save(p);
			}

		}
	}

}
