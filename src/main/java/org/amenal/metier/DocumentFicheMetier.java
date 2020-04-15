package org.amenal.metier;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.DocDesignationRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.DocumentRepository;
import org.amenal.dao.FicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Document;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.entities.fiches.DocFiche;
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
	private ProjetRepository projetRepository;

	@Autowired
	private DocumentMapper documentMapper;

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

		doc = new Document();
		doc.setIntitule(intitule);
		doc.setId(id);

		documentRepository.save(doc);
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
			throw new BadRequestException("Ce projet est inexistant!");

		if (ds.get().getDisponibilite())
			ds.get().setDisponibilite(false);
		else
			ds.get().setDisponibilite(true);

	}
}
