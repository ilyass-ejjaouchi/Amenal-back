package org.amenal.metier;

import java.util.Optional;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.BesionDesignationRepository;
import org.amenal.dao.FicheBesionRepository;
import org.amenal.entities.Article;
import org.amenal.entities.designations.BesionDesignation;
import org.amenal.entities.fiches.BesionFiche;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.BesionDesignationCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.amenal.rest.mapper.BesionDesignationMapper;

public class BesionFicheMetier {

	@Autowired
	FicheBesionRepository ficheBesionRepository;
	@Autowired
	BesionDesignationRepository besionDesignationRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	BesionDesignationMapper BesionDesignationMapper;

	public void addLigneDesignationBesion(BesionDesignationCommande bsrCmd) {

		BesionDesignation bs = BesionDesignationMapper.toEntity(bsrCmd);

		Optional<Article> ar = articleRepository.findById(bs.getArticle().getId());

		if (!ar.isPresent())
			throw new NotFoundException("Article introuvable!");

		Optional<BesionFiche> fiche = ficheBesionRepository.findById(bs.getBesionFiche().getId());

		if (!fiche.isPresent())
			throw new NotFoundException("Fiche introuvable!");

		bs.setArticle(ar.get());
		bs.setBesionFiche(fiche.get());

		besionDesignationRepository.save(bs);

	}

	public void updateLigneDesignationBesion(BesionDesignationCommande bsrCmd, Integer id) {

		BesionDesignation bs = BesionDesignationMapper.toEntity(bsrCmd);

		Optional<Article> ar = articleRepository.findById(bs.getArticle().getId());

		if (!ar.isPresent())
			throw new NotFoundException("Article introuvable!");

		Optional<BesionFiche> fiche = ficheBesionRepository.findById(bs.getBesionFiche().getId());

		if (!fiche.isPresent())
			throw new NotFoundException("Fiche introuvable!");

		bs.setArticle(ar.get());
		bs.setBesionFiche(fiche.get());
		bs.setId(id);

		besionDesignationRepository.save(bs);

	}

	public void DeleteLigneDesignationBesion(Integer id) {

		Optional<BesionDesignation> bs = besionDesignationRepository.findById(id);

		if (!bs.isPresent())
			throw new NotFoundException("designation introuvable!");

		besionDesignationRepository.delete(bs.get());

	}

}
