package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.amenal.metier.ReceptionFicheMetier;
import org.amenal.rest.commande.ReceptionDesignationCommande;
import org.amenal.rest.representation.ArticlePresentation;
import org.amenal.rest.representation.CategorieReceptionDesignationPresentation;
import org.amenal.rest.representation.FournisseurArticlePresentation;
import org.amenal.rest.representation.FournisseurPresentation;
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
@RequestMapping("/designations/receptions")
@CrossOrigin(origins = "*")
public class ReceptionDesignationController {
	
	@Autowired
	ReceptionFicheMetier receptionFicheMetier;
	
	@PreAuthorize("@authoritiesService.hasAuthorityFiche(#recCmd.idFiche ,'USER')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Void> AddLigneDesignationReception(@Valid @RequestBody ReceptionDesignationCommande recCmd) throws URISyntaxException {
		receptionFicheMetier.AddLigneDesignationReception(recCmd);
		return ResponseEntity.created(new URI("/designations/reception")).build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthorityFiche(#projetId ,'USER')")
	@RequestMapping(value = "/projets/{projetId}/fournisseurs", method = RequestMethod.GET)
	public List<FournisseurArticlePresentation> ListerMaterielAssoToProjet(@PathVariable Integer projetId) throws URISyntaxException {
		return receptionFicheMetier.ListerMaterielAssoToProjet(projetId);
	}
	
	@PreAuthorize("@authoritiesService.hasAuthorityDs(#idDs ,'USER')")
	@RequestMapping(value = "/{idDs}", method = RequestMethod.PUT)
	public void UpdateMaterielAssoToProjet(@Valid @RequestBody ReceptionDesignationCommande recCmd,@PathVariable Integer idDs) throws URISyntaxException {
		 receptionFicheMetier.UpdateLigneDesignationReception(recCmd, idDs);
	}
	
	@PreAuthorize("@authoritiesService.hasAuthorityDs(#idDs ,'USER')")
	@RequestMapping(value = "/{idDs}", method = RequestMethod.DELETE)
	public void DeleteMaterielAssoToProjet(@PathVariable Integer idDs) throws URISyntaxException {
		 receptionFicheMetier.SupprimerRecDesignation( idDs);
	}
	
	
	
	

}
