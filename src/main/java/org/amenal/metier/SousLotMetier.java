package org.amenal.metier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.EntreeRepository;
import org.amenal.dao.LotRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.QualificationOuvrierRepository;
import org.amenal.dao.SousLotDesignationRepository;
import org.amenal.dao.SousLotRepository;
import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Entree;
import org.amenal.entities.Lot;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.SousLot;
import org.amenal.entities.Unite;
import org.amenal.entities.designations.SousLotDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.EntreeCommande;
import org.amenal.rest.commande.SousLotCommande;
import org.amenal.rest.representation.BesoinRepresentation;
import org.amenal.rest.representation.EntreeNoAssoPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class SousLotMetier {

	@Autowired
	SousLotRepository sousLotRepository;
	@Autowired
	SousLotDesignationRepository sousLotDesignationRepository;
	@Autowired
	LotRepository lotRepository;
	@Autowired
	QualificationOuvrierRepository qualificationOuvrierRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	EntreeRepository entreeRepository;
	@Autowired
	UniteRepository uniteRepository;
	@Autowired
	ProjetRepository projetRepository;

	public void AssoSousLotToProjet(Integer sousLotId, Integer projetId) {

		Optional<SousLot> lot = sousLotRepository.findById(sousLotId);
		Optional<Projet> projet = projetRepository.findById(projetId);

		List<SousLotDesignation> ss = sousLotDesignationRepository
				.findSousLotDesignationByProjetIdWhereFicheNonValid(sousLotId, projetId);

		if (!ss.isEmpty())
			throw new BadRequestException(
					"Vous ne pouvez pas desassocier ce sous lot du projet courant , il' est associé a une fiche non valide!");

		if (projet.get().getSousLots().contains(lot.get())) {
			projet.get().getSousLots().remove(lot.get());
		} else {
			projet.get().getSousLots().add(lot.get());
		}

	}

	public void AddSousLot(SousLotCommande sLotCmd) {

		Optional<Lot> lot = lotRepository.findById(sLotCmd.getLotId());
		Unite unite = uniteRepository.findByUnite(sLotCmd.getUnite());

		if (!lot.isPresent())
			throw new BadRequestException("Le lot " + sLotCmd.getLotId() + " est introuvable!");

		for (SousLot slot : lot.get().getSousLots()) {
			if (sLotCmd.getDesignation().equals(slot.getDesignation())) {
				throw new BadRequestException("Ce Sous lot est deja existant!");
			}
		}

		SousLot slot = new SousLot();
		slot.setDesignation(sLotCmd.getDesignation());
		slot.setUnite(unite);
		slot.setLot(lot.get());
		sousLotRepository.save(slot);

	}

	public void UpdateSousLot(SousLotCommande sLotCmd, Integer id) {

		Optional<SousLot> slot = sousLotRepository.findById(id);
		Unite unite = uniteRepository.findByUnite(sLotCmd.getUnite());

		if (!slot.isPresent())
			throw new BadRequestException("Ce sous lot est introuvable!");

		slot.get().setDesignation(sLotCmd.getDesignation());
		slot.get().setUnite(unite);

	}

	public void DeleteSousLot(Integer id) {
		List<SousLotDesignation> ss = sousLotDesignationRepository.findSousLotDesignationWhereFicheNonValid(id);

		if (!ss.isEmpty())
			throw new BadRequestException(
					"Vous ne pouvez pas supprimer ce sous lot  , il' est associé a une fiche non valide!");

		sousLotRepository.deleteById(id);

	}

	public void AddEntreeToSousLot(EntreeCommande entreeCmd, Integer id) {

		Optional<SousLot> slot = sousLotRepository.findById(id);

		if (!slot.isPresent())
			throw new BadRequestException("Ce sous lot est introuvable!");

		Entree entree = new Entree();

		if (entreeCmd.getType() != null) {
			if (entreeCmd.getType().equals("ouv")) {

				Optional<QualificationOuvrier> qual = qualificationOuvrierRepository.findById(entreeCmd.getEntreeId());
				entree.setQualification(qual.get());
				entree.setQuantiteEstimer(entreeCmd.getQuantiteEstimer());
				entree.setType("ouv");
			} else if (entreeCmd.getType().equals("art")) {
				Optional<Article> art = articleRepository.findById(entreeCmd.getEntreeId());
				entree.setArticle(art.get());
				entree.setQuantiteEstimer(entreeCmd.getQuantiteEstimer());
				entree.setType("art");
			}
		}

		slot.get().getEntrees().add(entree);
		entree.setSousLot(slot.get());

	}

	public void updateEntree(EntreeCommande entreeCmd, Integer id) {

		Optional<Entree> entree = entreeRepository.findById(id);

		if (!entree.isPresent())
			throw new BadRequestException("Ce sous lot est introuvable!");

		if (entreeCmd.getType() != null) {
			if (entreeCmd.getType().equals("ouv")) {
				Optional<QualificationOuvrier> qual = qualificationOuvrierRepository.findById(entreeCmd.getEntreeId());
				entree.get().setQualification(qual.get());
				entree.get().setQuantiteEstimer(entreeCmd.getQuantiteEstimer());
				entree.get().setType("ouv");
			} else if (entreeCmd.getType().equals("art")) {
				Optional<Article> art = articleRepository.findById(entreeCmd.getEntreeId());
				entree.get().setArticle(art.get());
				entree.get().setQuantiteEstimer(entreeCmd.getQuantiteEstimer());
				entree.get().setType("art");
			}
		}

	}

	public List<EntreeNoAssoPresentation> listEntree() {

		List<EntreeNoAssoPresentation> listBesion = new ArrayList<EntreeNoAssoPresentation>();

		for (QualificationOuvrier qual : qualificationOuvrierRepository.findAll()) {
			EntreeNoAssoPresentation b = new EntreeNoAssoPresentation();
			b.setType("ouv");
			b.setId(qual.getId());
			b.setEntreeNom(qual.getCode());
			b.setUnite("H");
			listBesion.add(b);
		}

		for (Article art : articleRepository.findAll()) {
			EntreeNoAssoPresentation b = new EntreeNoAssoPresentation();
			b.setType("art");
			b.setId(art.getId());
			b.setEntreeNom(art.getDesignation());
			b.setUnite(art.getUnite().getUnite());

			listBesion.add(b);
		}

		return listBesion;

	}

	public void deleteEntree(Integer id) {
		

		this.entreeRepository.deleteById(id);
	}

}
