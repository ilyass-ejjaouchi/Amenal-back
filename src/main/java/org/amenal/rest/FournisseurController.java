package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.entities.Fournisseur;
import org.amenal.metier.FournisseurMetier;
import org.amenal.rest.commande.FournisseurCommande;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.representation.FournisseurPresentation;
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

import io.swagger.models.auth.In;

@RestController
@RequestMapping("/fournisseurs")
@CrossOrigin(origins = "*")
public class FournisseurController {
	

	@Autowired
	FournisseurMetier fournisseurMetier;

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Void> addFournisseur(@Valid @RequestBody FournisseurCommande frCmd) throws URISyntaxException {
		Fournisseur fr = fournisseurMetier.AjouterFournisseur(frCmd);
		
		return ResponseEntity.created(new URI("/materiels/".concat(fr.getId().toString()))).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'ADMIN')")
	@RequestMapping(value = "/projets/{idProjet}", method = RequestMethod.GET)
	public List<FournisseurPresentation> ListeFournisseurByProjet(@PathVariable(name = "idProjet")Integer idProjet) throws URISyntaxException {
		return fournisseurMetier.ListerFournisseur(idProjet);
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{idFournisseur}/materiels/{idMateriel}", method = RequestMethod.PUT)
	public ResponseEntity<Void> AssosierMaterielToFournisseur(@PathVariable(name = "idFournisseur") Integer idFourniseur , @PathVariable(name = "idMateriel")Integer idMateriel  ) throws URISyntaxException {

		fournisseurMetier.AssosierMaterielToFournisseur(idFourniseur, idMateriel);
		
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{idFournisseur}/materiels/{idMateriel}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> DesassosierMaterielToFournisseur(@PathVariable(name = "idFournisseur") Integer idFourniseur , @PathVariable(name = "idMateriel")Integer idMateriel  ) throws URISyntaxException {
		
		fournisseurMetier.DesassosierMaterielToFournisseur(idFourniseur, idMateriel);
		
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'ADMIN')")
	@RequestMapping(value = "/{idFournisseur}/projets/{idProjet}", method = RequestMethod.PUT)
	public ResponseEntity<Void> AssosierFournisseurToProjet(@PathVariable(name = "idFournisseur") Integer idFourniseur , @PathVariable(name = "idProjet")Integer idProjet  ) throws URISyntaxException {
		
		fournisseurMetier.AssosierFournisseurToProjet(idFourniseur, idProjet);
		
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{fourID}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateFournisseur(@PathVariable Integer fourID, @Valid @RequestBody FournisseurCommande fourCmd)
			throws URISyntaxException {
		fournisseurMetier.modifierFourniseur(fourCmd, fourID);
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{fourID}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteFournisseurFromLocation(@PathVariable Integer fourID ,@RequestParam Boolean ctn)
			throws URISyntaxException {
		fournisseurMetier.supprimerFourniseurFromFicheLocation(fourID , ctn);
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{fourID}/receptions", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteFournisseurFromReception(@PathVariable Integer fourID )
			throws URISyntaxException {
		fournisseurMetier.supprimerFourniseurFromFicheReception(fourID);
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'ADMIN') && @authoritiesService.hasAuthority(#idProjet,'ADMIN')")
	@RequestMapping(value = "/projets/{idProjet}/materiels/{idMateriel}", method = RequestMethod.GET)
	public List<FournisseurPresentation> ListeFournisseurByProjetAndMateriel(@PathVariable(name = "idProjet")Integer idProjet 
			, @PathVariable(name = "idMateriel")Integer idMateriel) throws URISyntaxException {
		return fournisseurMetier.ListerFournisseurByProjetAndMateriel(idProjet,idMateriel);
	}
	

}
