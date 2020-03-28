package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.metier.LocationFicheMetier;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.commande.LocationDesignationCommande;
import org.amenal.rest.representation.FournisseurArticlePresentation;
import org.amenal.rest.representation.FournisseurPresentation;
import org.amenal.rest.representation.LocationDesignationPresentation;
import org.amenal.rest.representation.ProjetPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations/locations")
@CrossOrigin("*")
public class LocationDesignationController {

	@Autowired
	LocationFicheMetier locationFicheMetier;

	@RequestMapping(value = "/projets/{projetId}/fournisseurs", method = RequestMethod.GET)

	public List<FournisseurPresentation> ListerFournisseurAssoToProjet(@PathVariable Integer projetId)
			throws URISyntaxException {
		return locationFicheMetier.ListMaterielAssoToProjet(projetId);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)

	public ResponseEntity<Void> addLocationDesignation(@Valid @RequestBody LocationDesignationCommande dsCmd)
			throws URISyntaxException {

		LocationDesignation LocationDesignation = locationFicheMetier.addLigneDesignation(dsCmd);
		return ResponseEntity.created(new URI("/designations/".concat(LocationDesignation.getId().toString()))).build();
	}

	@RequestMapping(value = "/{locDsId}", method = RequestMethod.DELETE)
	public void SupprimerLocationDesignation(@PathVariable Integer locDsId) throws URISyntaxException {
		locationFicheMetier.SupprimerLocationDesignation(locDsId);

	}

	@RequestMapping(value = "/{locDsId}", method = RequestMethod.PUT)
	public void ModifierLocationDesignation(@Valid @RequestBody LocationDesignationCommande dsCmd,
			@PathVariable Integer locDsId) throws URISyntaxException {
		
		locationFicheMetier.updateLigneDesignationLocation(dsCmd, locDsId);

	}

}
