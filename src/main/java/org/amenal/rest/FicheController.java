package org.amenal.rest;

import java.net.URISyntaxException;

import org.amenal.entities.fiches.LocationFiche;
import org.amenal.metier.AccidentFicheMetier;
import org.amenal.metier.BesoinFicheMetier;
import org.amenal.metier.DocumentFicheMetier;
import org.amenal.metier.FicheVisiteurMetier;
import org.amenal.metier.LivraisonFicheMetier;
import org.amenal.metier.LocationFicheMetier;
import org.amenal.metier.OuvrierFicheMetier;
import org.amenal.metier.ReceptionFicheMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@Autowired
	LocationFicheMetier locationFicheMetier;

	@Autowired
	ReceptionFicheMetier receptionFicheMetier;

	@Autowired
	LivraisonFicheMetier livraisonFicheMetier;

	@Autowired
	DocumentFicheMetier documentFicheMetier;
	
	@Autowired
	AccidentFicheMetier  accidentFicheMetier;
	
	@Autowired
	BesoinFicheMetier besoinFicheMetier;
	
	@Autowired
	FicheVisiteurMetier ficheVisiteurMetier;
	
	
	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/accidents/{ficheId}", method = RequestMethod.PUT)
	public void validerFicheAccident(@PathVariable Integer ficheId) throws URISyntaxException {
		accidentFicheMetier.validerFicheAccident(ficheId);
	}

	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/documents/{ficheId}", method = RequestMethod.PUT)
	public void ValiderFicheDocument(@PathVariable Integer ficheId) throws URISyntaxException {
		documentFicheMetier.validerFichedocumet(ficheId);

	}

	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/ouvriers/{ficheId}", method = RequestMethod.PUT)
	public void ValiderFicheOuvirer(@PathVariable Integer ficheId) throws URISyntaxException {
		ouvrierFicheMetier.ValiderFicheOuvrier(ficheId);

	}

	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/locations/{ficheId}", method = RequestMethod.PUT)
	public void validerFicheLocation(@PathVariable Integer ficheId) throws URISyntaxException {
		locationFicheMetier.validerFicheLocation(ficheId);
	}

	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/receptions/{ficheId}", method = RequestMethod.PUT)
	public void validerFicheReception(@PathVariable Integer ficheId) throws URISyntaxException {
		receptionFicheMetier.validerFicheReception(ficheId);
	}

	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/livraisons/{ficheId}", method = RequestMethod.PUT)
	public void validerFicheLivraison(@PathVariable Integer ficheId) throws URISyntaxException {
		livraisonFicheMetier.validerFicheLivraison(ficheId);
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/besoins/{ficheId}", method = RequestMethod.PUT)
	public void validerFicheBesoin(@PathVariable Integer ficheId) throws URISyntaxException {
		besoinFicheMetier.validerFicheBesion(ficheId);
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/visiteurs/{ficheId}", method = RequestMethod.PUT)
	public void validerFicheVisiteur(@PathVariable Integer ficheId) throws URISyntaxException {
		ficheVisiteurMetier.validerVisiteur(ficheId);
	}
	

}
