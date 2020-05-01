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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations")
@CrossOrigin("*")
public class OuvrierDesignationController {

	@Autowired
	OuvrierFicheMetier ouvrierFicheMetier;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@PreAuthorize("@authoritiesService.hasAuthority(#OuvrierDesignationCommande.idFiche,'USER')")
	public ResponseEntity<Void> addOuvrierDesignation(@Valid @RequestBody OuvrierDesignationCommande dsCmd)
			throws URISyntaxException {

		OuvrierDesignation ouvrierDesignation = ouvrierFicheMetier.addLigneDesignation(dsCmd);
		return ResponseEntity.created(new URI("/designations/".concat(ouvrierDesignation.getId().toString()))).build();
	}
	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<OuvrierDesignationPresentation> getListOuvrierDesignation() {
		return ouvrierFicheMetier.ListOuvrierDesignation();
	}
	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/{OuvId}", method = RequestMethod.DELETE)
	public void SupprimerOuvrierDesignation(@PathVariable Integer OuvId) {
		ouvrierFicheMetier.SupprimerOuvrierDesignation(OuvId);

	}
	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/{OuvDsId}", method = RequestMethod.PUT)
	public void SupprimerOuvrierDesignation(@PathVariable Integer OuvDsId , @Valid @RequestBody OuvrierDesignationCommande dsCmd) {
		ouvrierFicheMetier.updateLigneDesignation(dsCmd ,OuvDsId);

	}
}
