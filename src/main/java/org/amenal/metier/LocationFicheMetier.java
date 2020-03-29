package org.amenal.metier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.FournisseurRepository;
import org.amenal.dao.LocationAssoRepository;
import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.LocationFicheRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.LocationDesignationCommande;
import org.amenal.rest.mapper.FournisseurMapper;
import org.amenal.rest.mapper.LocationDesignationMapper;
import org.amenal.rest.representation.FournisseurPresentation;
import org.amenal.rest.representation.LocationDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationFicheMetier {

	@Autowired
	private LocationFicheRepository locationFicheRepository;
	@Autowired
	private LocationDesignationRepository locationDesignationRepository;
	@Autowired
	private LocationDesignationMapper locationDesignationMapper;
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private FournisseurRepository fournisseurRepository;
	@Autowired
	private FournisseurMapper fournisseurMapper;
	@Autowired
	private LocationAssoRepository locationAssoRepository;

	public LocationDesignation addLigneDesignation(@Valid LocationDesignationCommande dsCmd) {
		// TODO Auto-generated method stub
		LocationDesignation locDs = locationDesignationMapper.toEntity(dsCmd);
		Integer ficheId = locDs.getLocationFiche().getId();
		Optional<LocationFiche> fiche = locationFicheRepository.findById(ficheId);
		Optional<Article> article = articleRepository.findById(locDs.getArticle().getId());
		Optional<Fournisseur> fr = fournisseurRepository.findById(locDs.getFournisseur().getId());

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + ficheId + " ] est introuvable !");
		if (!article.isPresent())
			throw new NotFoundException("L' article [ " + locDs.getArticle().getId() + " ] est introuvable !");
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + locDs.getFournisseur().getId() + " ] est introuvable !");

		locDs.setLibelle(article.get().getDesignation());
		locDs.setUnite(article.get().getUnite().getUnite());
		locDs.setFournisseurNom(fr.get().getFournisseurNom());
		locDs.setLocationFiche(fiche.get());
		locDs.setArticle(article.get());
		locDs.setFournisseur(fr.get());

		return locationDesignationRepository.save(locDs);
	}

	public List<FournisseurPresentation> ListMaterielAssoToProjet(Integer projetId) {

		List<Fournisseur> frs = new ArrayList<Fournisseur>();

		List<LocationAsso> assos = locationAssoRepository.findByProjet(projetId);

		assos.forEach(asso -> {
			Fournisseur f = new Fournisseur();
			f.setId(asso.getFourniseur().getId());
			f.setFournisseurNom(asso.getFourniseur().getFournisseurNom());
			f.setMateriel(asso.getArticle());

			frs.add(f);
		});

		return frs.stream().map(e -> fournisseurMapper.toRepresentation(e)).collect(Collectors.toList());
	}

	public void SupprimerLocationDesignation(Integer locDsId) {
		// TODO Auto-generated method stub
		Optional<LocationDesignation> ds = locationDesignationRepository.findById(locDsId);
		if (!ds.isPresent())
			throw new NotFoundException("La ligne [ " + locDsId + " ] est introuvable !");

		this.locationDesignationRepository.delete(ds.get());

	}

	public void updateLigneDesignationLocation( LocationDesignationCommande dsCmd, Integer locDsId) {
		// TODO Auto-generated method stub
		
		LocationDesignation locDs = locationDesignationMapper.toEntity(dsCmd);
		Integer ficheId = locDs.getLocationFiche().getId();
		Optional<LocationFiche> fiche = locationFicheRepository.findById(ficheId);
		Optional<Article> article = articleRepository.findById(locDs.getArticle().getId());
		Optional<Fournisseur> fr = fournisseurRepository.findById(locDs.getFournisseur().getId());

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + ficheId + " ] est introuvable !");
		if (!article.isPresent())
			throw new NotFoundException("L' article [ " + locDs.getArticle().getId() + " ] est introuvable !");
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + locDs.getFournisseur().getId() + " ] est introuvable !");
		locDs.setId(locDsId);
		locDs.setLibelle(article.get().getDesignation());
		locDs.setUnite(article.get().getUnite().getUnite());
		locDs.setFournisseurNom(fr.get().getFournisseurNom());
		locDs.setLocationFiche(fiche.get());
		locDs.setArticle(article.get());
		locDs.setFournisseur(fr.get());

		 locationDesignationRepository.save(locDs);
		
		

	}

}