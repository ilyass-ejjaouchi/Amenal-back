package org.amenal.rest;


import org.amenal.metier.OuvrierFicheMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fiches")
@CrossOrigin("*")
public class FicheController {
	
	@Autowired
	OuvrierFicheMetier ouvrierFicheMetier;
	
	@RequestMapping(value = "/{ficheId}", method = RequestMethod.PUT)
	public void SupprimerOuvrierDesignation(@PathVariable Integer ficheId ) {
		ouvrierFicheMetier.ValiderFicheOuvrier(ficheId);

	}

	
}
