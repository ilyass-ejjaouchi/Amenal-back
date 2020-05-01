package org.amenal.rest;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.amenal.metier.LivraisonFicheMetier;
import org.amenal.rest.commande.LivraisonDesignationCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations/livraisons")
@CrossOrigin("*")
public class LivraisonController {

	@Autowired
	LivraisonFicheMetier livraisonFicheMetier;

	@PreAuthorize(" @authoritiesService.hasAuthorityFiche(#lvCmd.idFiche ,'USER')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void addLivraisonDesignation(@Valid @RequestBody LivraisonDesignationCommande lvCmd)
			throws URISyntaxException {
		this.livraisonFicheMetier.addLivraisonDesignation(lvCmd);
	}

	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#id ,'USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void updateLivraisonDesignation(@Valid @RequestBody LivraisonDesignationCommande lvCmd,
			@PathVariable Integer id) throws URISyntaxException {
		this.livraisonFicheMetier.updateLivraisonDesignation(lvCmd, id);
	}
	
	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#id ,'USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteLivraisonDesignation(@PathVariable Integer id) throws URISyntaxException {
		this.livraisonFicheMetier.deleteLivraisonDesignation(id);
	}

}
