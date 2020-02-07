package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.metier.OuvrierFicheMetier;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.commande.OuvrierDesignationCommande;
import org.amenal.rest.representation.OuvrierDesignationPresentation;
import org.amenal.rest.representation.ProjetPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations")
public class DesignationController {

	@Autowired
	OuvrierFicheMetier ouvrierFicheMetier;

	@RequestMapping(value = "", method = RequestMethod.POST)

	public ResponseEntity<Void> addOuvrierDesignation(@Valid @RequestBody OuvrierDesignationCommande dsCmd)
			throws URISyntaxException {

		OuvrierDesignation ouvrierDesignation = ouvrierFicheMetier.addLigneDesignation(dsCmd);
		return ResponseEntity.created(new URI("/designations/".concat(ouvrierDesignation.getId().toString()))).build();
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<OuvrierDesignationPresentation> getListOuvrierDesignation() {
		return ouvrierFicheMetier.ListOuvrierDesignation();
	}
}
