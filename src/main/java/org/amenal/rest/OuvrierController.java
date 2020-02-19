package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.entities.Ouvrier;
import org.amenal.metier.OuvrierMetier;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.representation.OuvrierPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ouvriers")
@CrossOrigin(origins = "*")
public class OuvrierController {
	
	@Autowired
	OuvrierMetier ouvrierMetier;
	
	@RequestMapping(value = "", method = RequestMethod.POST)

	public  ResponseEntity<Void> addOuvrier(@Valid @RequestBody OuvrierCommande ouvCmd) throws URISyntaxException{
		Ouvrier ouvrier =  ouvrierMetier.ajouterOuvrier(ouvCmd);
			return ResponseEntity.created(new URI("/projet/".concat(ouvrier.getId().toString()))).build();
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)

	public  List<OuvrierPresentation> ListeOuvrier() throws URISyntaxException{
		return ouvrierMetier.ListerOuvriers();
	}
	
}
