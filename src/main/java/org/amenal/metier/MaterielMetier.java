package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.LocationAssoRepository;
import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Projet;
import org.amenal.entities.Unite;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.mapper.MaterielMapper;
import org.amenal.rest.representation.MaterielPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MaterielMetier {

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	MaterielMapper materielMapper;

	@Autowired
	LocationAssoRepository locationAssoRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	LocationDesignationRepository locationDesignationRepository;

	@Autowired
	UniteRepository uniteRepository;

	public void ajouterMateriel(MaterielCommande materielCmd) {

		Unite unite = uniteRepository.findByUnite(materielCmd.getUnite());

		if (unite == null)
			throw new NotFoundException("l' unité  [ " + materielCmd.getUnite() + " ] est inexistante!");

		Article materiel = materielMapper.toEntity(materielCmd);
		materiel.setUnite(unite);
		this.articleRepository.save(materiel);

	}

	public List<MaterielPresentation> ListerMateriel() {

		return this.articleRepository.findAllMateriels().stream().map(o -> materielMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void modifierMateriel(MaterielCommande matCmd, Integer matID) {

		Optional<Article> mtr = articleRepository.findById(matID);
		Unite unite = uniteRepository.findByUnite(matCmd.getUnite());

		if (unite == null)
			throw new NotFoundException("l' unité  [ " + matCmd.getUnite() + " ] est inexistante!");
		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + matID + "] est inexistant");

		matCmd.setId(matID);
		Article a = materielMapper.toEntity(matCmd);
		a.setUnite(unite);

		/*****/
		List<LocationDesignation> ds = locationDesignationRepository.findDesignationByArticleIDAndFicheNotValid(matID);
		if (!ds.isEmpty())
			ds.forEach(d -> {
				d.setLibelle(a.getDesignation());
				d.setUnite(a.getUnite().getUnite());
			});
		/*****/

		this.articleRepository.save(a);
	}

	public void SupprimerMateriel(Integer matID) {

		Optional<Article> mtr = articleRepository.findById(matID);
		
		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + matID + "] est inexistant");

		List<LocationAsso> locs = locationAssoRepository.findByArticle(mtr.get());

		if (!locs.isEmpty()) {
			System.out.println("ddddd"+locs);
			throw new BadRequestException(
					"Vous ne pouvez pas supprimer le materiel [ " + mtr.get().getDesignation() + " ] !");
		}else {
			mtr.get().setUnite(null);
			articleRepository.delete(mtr.get());
		}

	}

}
