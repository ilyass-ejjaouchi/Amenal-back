package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.CategorieArticleRepository;
import org.amenal.dao.ReceptionAssoRepository;
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.ReceptionAsso;
import org.amenal.entities.Unite;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.ArticleCommande;
import org.amenal.rest.commande.CategorieArticleCommande;
import org.amenal.rest.mapper.ArticleMapper;
import org.amenal.rest.mapper.CategorieArticleMapper;
import org.amenal.rest.representation.CategorieArticlePresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleMetier {

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	CategorieArticleRepository categorieArticleRepository;

	@Autowired
	UniteRepository uniteRepository;

	@Autowired
	ReceptionDesignationRepository receptionDesignationRepository;

	@Autowired
	ReceptionAssoRepository receptionAssoRepository;

	@Autowired
	ArticleMapper articleMapper;

	@Autowired
	CategorieArticleMapper categorieArticleMapper;

	public Integer addCategorieArticle(CategorieArticleCommande CatCmd) {

		CategorieArticle cat = categorieArticleMapper.toEntity(CatCmd);

		CategorieArticle cc = categorieArticleRepository.findByCategorie(cat.getCategorie());

		if (cc != null)
			if (cc.getShowCat())
				throw new NotFoundException("l' acategorie  [ " + cat.getCategorie() + " ] est deja existante!");
			else {
				cc.setShowCat(true);
				return cc.getId();
			}

		else {
			cat.setShowCat(true);
			return categorieArticleRepository.save(cat).getId();
		}
	}

	public void editCategorieArticle(CategorieArticleCommande CatCmd, Integer id) {
		CategorieArticle cc = categorieArticleMapper.toEntity(CatCmd);
		
		CategorieArticle ca = categorieArticleRepository.findByCategorie(cc.getCategorie());
		


		Optional<CategorieArticle> cat = categorieArticleRepository.findById(id);
		
		

		if (!cat.isPresent())
			throw new NotFoundException("La categorie dont l' id [ " + id + " ] est inexistante!");

		List<ReceptionDesignation> dss = receptionDesignationRepository.findDesignationByCategorieAndFicheNotValid(id);

		if (!dss.isEmpty()) {
			dss.forEach(l -> {
				l.setCategorie(CatCmd.getCategorie());
			});
		}

		CatCmd.setId(id);


		cc.setShowCat(true);

		categorieArticleRepository.save(cc);

	}

	public Integer EditArticle(ArticleCommande articleCmd, Integer id) {

		Optional<CategorieArticle> cat = categorieArticleRepository.findById(articleCmd.getCategorieID());
		Unite unite = uniteRepository.findByUnite(articleCmd.getUnite());
		if (!cat.isPresent())
			throw new NotFoundException(
					"la categorie dont l' id [ " + articleCmd.getCategorieID() + " ] est inexistante!");
		if (unite == null)
			throw new NotFoundException("l' unité  [ " + articleCmd.getUnite() + " ] est inexistante!");

		Article article = articleMapper.toEntity(articleCmd);

		List<ReceptionDesignation> dss = receptionDesignationRepository.findDesignationByArticleIDAndFicheNotValid(id);

		if (!dss.isEmpty()) {
			dss.forEach(l -> {
				l.setLibelle(article.getDesignation());
				l.setUnitee(unite.getUnite());
			});
		}
		article.setShowArt(true);
		article.setUnite(unite);

		article.setId(id);

		return articleRepository.save(article).getId();
	}

	public void supprimerCategorie(Integer catId) {
		Optional<CategorieArticle> cat = categorieArticleRepository.findById(catId);

		if (!cat.isPresent())
			throw new NotFoundException("Cette categorie est introuvable!");

		List<Article> articles = receptionAssoRepository.findArticleByCat(cat.get().getCategorie());

		if (!articles.isEmpty())
			throw new BadRequestException("Cette categorie est deja associer des fournisseur!");

		articles.forEach(a -> {
			a.setShowArt(false);
		});

		cat.get().setShowCat(false);

		// categorieArticleRepository.delete(cat.get());

	}

	public void supprimerArticle(Integer artId) {
		Optional<Article> ar = articleRepository.findById(artId);

		if (!ar.isPresent())
			throw new NotFoundException("Cet article est introuvable!");

		List<ReceptionAsso> assos = receptionAssoRepository.findByArticle(ar.get());

		if (!assos.isEmpty())
			throw new BadRequestException("Cette categorie est deja associer des fournisseur!");

		ar.get().setShowArt(false);

		// articleRepository.delete(ar.get());

	}

	public Integer AddArticle(ArticleCommande articleCmd) {

		Optional<CategorieArticle> cat = categorieArticleRepository.findById(articleCmd.getCategorieID());
		Unite unite = uniteRepository.findByUnite(articleCmd.getUnite());
		if (!cat.isPresent())
			throw new NotFoundException(
					"la categorie dont l' id [ " + articleCmd.getCategorieID() + " ] est inexistante!");
		if (unite == null)
			throw new NotFoundException("l' unité  [ " + articleCmd.getUnite() + " ] est inexistante!");

		Article article = articleMapper.toEntity(articleCmd);

		article.setUnite(unite);

		Article art = articleRepository.findByDesignation(article.getDesignation());

		if (art != null) {
			if (art.getShowArt())
				throw new NotFoundException("l' article  [" + art.getDesignation() + " ] est deja existant!");
			else {
				art.setCategorie(cat.get());
				art.setShowArt(true);
				return art.getId();
			}

		} else {
			article.setShowArt(true);

			return articleRepository.save(article).getId();
		}

	}

	public List<CategorieArticlePresentation> ListerArticle() {

		List<CategorieArticle> cats = categorieArticleRepository.findByShow(true);

		return cats.stream().map(c -> {
			CategorieArticle cc = new CategorieArticle();
			cc.setId(c.getId());
			cc.setCategorie(c.getCategorie());
			cc.setIsAssoWithProjet(c.getIsAssoWithProjet());
			c.getArticles().forEach(a -> {
				if (a.getShowArt()) {
					Article aa = new Article();
					aa.setId(a.getId());
					aa.setDesignation(a.getDesignation());
					aa.setIsAssoWithProjet(a.getIsAssoWithProjet());
					aa.setShowArt(true);
					aa.setUnite(a.getUnite());
					aa.setStockable(a.getStockable());
					cc.getArticles().add(aa);
				}
			});
			return categorieArticleMapper.toRepresentation(cc);
		}).collect(Collectors.toList());

	}

}
