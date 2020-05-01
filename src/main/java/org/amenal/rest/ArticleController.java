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
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/categories", method = RequestMethod.POST)
	public ResponseEntity<Void> addCategorie(@Valid @RequestBody CategorieArticleCommande categorieCmd)
			throws URISyntaxException {

		Integer id = this.articleMetier.addCategorieArticle(categorieCmd);
		return ResponseEntity.created(new URI("/Articles/categories".concat(id.toString()))).build();

	}
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.PUT)
	public void editerCategorie(@Valid @RequestBody CategorieArticleCommande categorieCmd, @PathVariable Integer id)
			throws URISyntaxException {

		this.articleMetier.editCategorieArticle(categorieCmd, id);

	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Void> addArticle(@Valid @RequestBody ArticleCommande articleCmd) throws URISyntaxException {

		Integer id = this.articleMetier.AddArticle(articleCmd);
		return ResponseEntity.created(new URI("/Articles/".concat(id.toString()))).build();

	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void editerArticle(@Valid @RequestBody ArticleCommande articleCmd, @PathVariable Integer id)
			throws URISyntaxException {

		this.articleMetier.EditArticle(articleCmd, id);

	}
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/categories/{id}", method = RequestMethod.DELETE)
	public void supprimerCategorieWithArticles(@PathVariable Integer id) throws URISyntaxException {

		this.articleMetier.supprimerCategorie(id);

	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void supprimerArticle(@PathVariable Integer id) throws URISyntaxException {

		this.articleMetier.supprimerArticle(id);

	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public List<CategorieArticlePresentation> listeCategoriesWithArticle() throws URISyntaxException {

		return articleMetier.ListerArticle();
	}

}
