package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.amenal.entities.Ouvrier;
import org.amenal.metier.UniteMetier;
import org.amenal.rest.commande.OuvrierCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unites")
@CrossOrigin(origins = "*")
public class UniteController {
	
	@Autowired
	UniteMetier uniteMetier;
	
	@RequestMapping(value = "", method = RequestMethod.POST)

	public ResponseEntity<Void> addUnite(@Valid @RequestBody String unite) throws URISyntaxException {
		 uniteMetier.AddUnite(unite);
		return ResponseEntity.created(new URI("/unites")).build();
	}

}