package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.amenal.entities.Ouvrier;
import org.amenal.metier.UniteMetier;
import org.amenal.rest.commande.OuvrierCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<String> getUnite() throws URISyntaxException {
		return uniteMetier.getUnite();
	}
	
	@RequestMapping(value = "/{unite}", method = RequestMethod.DELETE)
	public void deleteUnite(@PathVariable("unite") String unite) throws URISyntaxException {
		 uniteMetier.deleteUnite(unite);
	}

}
