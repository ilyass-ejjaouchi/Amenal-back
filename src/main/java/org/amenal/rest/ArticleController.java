package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.ArticleMetier;
import org.amenal.rest.commande.ArticleCommande;
import org.amenal.rest.commande.CategorieArticleCommande;
import org.amenal.rest.representation.CategorieArticlePresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
@CrossOrigin("*")
public class ArticleController {

	@Autowired
	ArticleMetier articleMetier;

	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public ResponseEntity<Void> addCategorie(@Valid @RequestBody CategorieArticleCommande categorieCmd)
			throws URISyntaxException {

		Integer id = this.articleMetier.addCategorieArticle(categorieCmd);
		return ResponseEntity.created(new URI("/Articles/categories".concat(id.toString()))).build();

	}
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.PUT)
	public void editerCategorie(@Valid @RequestBody CategorieArticleCommande categorieCmd , @PathVariable Integer id)
			throws URISyntaxException {

		this.articleMetier.editCategorieArticle(categorieCmd , id);

	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Void> addArticle(@Valid @RequestBody ArticleCommande articleCmd) throws URISyntaxException {

		Integer id = this.articleMetier.AddArticle(articleCmd);
		return ResponseEntity.created(new URI("/Articles/".concat(id.toString()))).build();

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void editerArticle(@Valid @RequestBody ArticleCommande articleCmd , @PathVariable Integer id)
			throws URISyntaxException {

		this.articleMetier.EditArticle(articleCmd , id);

	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public List<CategorieArticlePresentation> listeCategoriesWithArticle() throws URISyntaxException {

		return articleMetier.ListerArticle();
	}

}