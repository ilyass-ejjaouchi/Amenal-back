package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.CategorieArticleRepository;
import org.amenal.dao.ReceptionAssoRepository;
import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.ReceptionAsso;
import org.amenal.entities.Unite;
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
	ReceptionAssoRepository receptionAssoRepository;

	@Autowired
	ArticleMapper articleMapper;

	@Autowired
	CategorieArticleMapper categorieArticleMapper;

	public Integer addCategorieArticle(CategorieArticleCommande CatCmd) {

		CategorieArticle cat = categorieArticleMapper.toEntity(CatCmd);

		return categorieArticleRepository.save(cat).getId();
	}

	public void editCategorieArticle(CategorieArticleCommande CatCmd, Integer id) {

		Optional<CategorieArticle> cat = categorieArticleRepository.findById(id);

		if (!cat.isPresent())
			throw new NotFoundException("La categorie dont l' id [ " + id + " ] est inexistante!");

		CatCmd.setId(id);

		categorieArticleRepository.save(categorieArticleMapper.toEntity(CatCmd));

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

		categorieArticleRepository.delete(cat.get());

	}
	
	public void supprimerArticle(Integer artId) {
		Optional<Article> ar = articleRepository.findById(artId);

		if (!ar.isPresent())
			throw new NotFoundException("Cet article est introuvable!");

		List<ReceptionAsso> assos = receptionAssoRepository.findByArticle(ar.get());

		if (!assos.isEmpty())
			throw new BadRequestException("Cette categorie est deja associer des fournisseur!");

		articleRepository.delete(ar.get());

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

		return articleRepository.save(article).getId();
	}

	public List<CategorieArticlePresentation> ListerArticle() {

		return categorieArticleRepository.findAll().stream().map(c -> categorieArticleMapper.toRepresentation(c))
				.collect(Collectors.toList());

	}

}
