package org.amenal.rest;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.amenal.metier.AccidentFicheMetier;
import org.amenal.rest.commande.AccidentDesignationCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations/accidents")
@CrossOrigin("*")
public class FicheAccidentController {
	
	@Autowired
	AccidentFicheMetier accidentFicheMetier;
	
	@PreAuthorize(" @authoritiesService.hasAuthorityFiche(#acCmd.ficheID ,'USER')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void addAccidentDesignation(@Valid @RequestBody AccidentDesignationCommande acCmd) throws URISyntaxException {
		accidentFicheMetier.AddAccidentDesignation(acCmd);
	}
	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#id ,'USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void updateAccidentDesignation(  @PathVariable Integer id , @Valid @RequestBody AccidentDesignationCommande acCmd) throws URISyntaxException {
		accidentFicheMetier.UpdateAccidentDesignation(acCmd , id);
	}
	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#id ,'USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteAccidentDesignation(  @PathVariable Integer id ) throws URISyntaxException {
		accidentFicheMetier.deleteAccidentDesignation(id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
