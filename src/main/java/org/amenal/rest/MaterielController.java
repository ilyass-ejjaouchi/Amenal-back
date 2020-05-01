package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.entities.Ouvrier;
import org.amenal.metier.MaterielMetier;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.representation.MaterielPresentation;
import org.amenal.rest.representation.OuvrierPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/materiels")
@CrossOrigin(origins = "*")
public class MaterielController {

	@Autowired
	MaterielMetier materielMetier;

	@RequestMapping(value = "", method = RequestMethod.POST)

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	public ResponseEntity<Void> addMateriel(@Valid @RequestBody MaterielCommande matCmd) throws URISyntaxException {
		 materielMetier.ajouterMateriel(matCmd);
		return ResponseEntity.created(new URI("/materiels/")).build();
	}

	@PreAuthorize(" @authoritiesService.hasAuthority('ADMIN') ")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<MaterielPresentation> ListeMateriel() throws URISyntaxException {
		return materielMetier.ListerMateriel();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	  @RequestMapping(value = "/{matId}", method = RequestMethod.DELETE) 
	  public void deleteMateriel(@PathVariable Integer matId ) throws URISyntaxException {
		  materielMetier.SupprimerMateriel(matId);
	  
	  }
	 
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{matID}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateMateriel(@PathVariable Integer matID, @Valid @RequestBody MaterielCommande matCmd)
			throws URISyntaxException {
		materielMetier.modifierMateriel(matCmd, matID);
		return ResponseEntity.ok().build();
	}


}
