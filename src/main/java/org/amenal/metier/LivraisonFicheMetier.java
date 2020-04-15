package org.amenal.metier;

import java.util.Optional;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.DestinationRepository;
import org.amenal.dao.LivraisonDesignationRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Destination;
import org.amenal.entities.designations.Designation;
import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.LivraisonDesignationCommande;
import org.amenal.rest.mapper.LivraisonDesignationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LivraisonFicheMetier {

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	DestinationRepository destinationRepository;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	LivraisonDesignationRepository livraisonDesignationRepository;

	@Autowired
	LivraisonDesignationMapper livraisonDesignationMapper;

	public void addLivraisonDesignation(LivraisonDesignationCommande livCmd) {

		LivraisonDesignation ds = livraisonDesignationMapper.toEntity(livCmd);

		Optional<Article> art = articleRepository.findById(ds.getArticleLvr().getId());
		Optional<Destination> dst = destinationRepository.findById(ds.getDestination().getId());
		if (!art.isPresent())
			throw new NotFoundException("L 'article [ " + ds.getArticleLvr().getId() + " ] est introuvable!");
		if (!dst.isPresent())
			throw new NotFoundException("La destination [ " + ds.getArticleLvr().getId() + " ] est introuvable!");

		ds.setArticleLvr(art.get());
		ds.setCategorieLv(art.get().getCategorie().getCategorie());
		ds.setUnite(art.get().getUnite().getUnite());
		ds.setDestination(dst.get());

		livraisonDesignationRepository.save(ds);
	}
	
	public void updateLivraisonDesignation(LivraisonDesignationCommande livCmd , Integer id) {

		LivraisonDesignation ds = livraisonDesignationMapper.toEntity(livCmd);

		Optional<Article> art = articleRepository.findById(ds.getArticleLvr().getId());
		Optional<Destination> dst = destinationRepository.findById(ds.getDestination().getId());
		if (!art.isPresent())
			throw new NotFoundException("L 'article [ " + ds.getArticleLvr().getId() + " ] est introuvable!");
		if (!dst.isPresent())
			throw new NotFoundException("La destination [ " + ds.getArticleLvr().getId() + " ] est introuvable!");

		ds.setArticleLvr(art.get());
		ds.setCategorieLv(art.get().getCategorie().getCategorie());
		ds.setDestination(dst.get());
		ds.setId(id);

		livraisonDesignationRepository.save(ds);
	}
	
	public void deleteLivraisonDesignation(Integer id) {
	 Optional<LivraisonDesignation> ds=	livraisonDesignationRepository.findById(id);
	 if(!ds.isPresent())
		 throw new NotFoundException("La designation livraison ["+ id +" est introuvable");
	 livraisonDesignationRepository.delete(ds.get());
	}

}
