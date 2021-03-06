package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.amenal.entities.Projet;
import org.amenal.metier.FournisseurMetier;
import org.amenal.metier.ProjetMetier;
import org.amenal.rest.commande.FicheCommande;
import org.amenal.rest.commande.ProjetCommande;
import org.amenal.rest.mapper.OuvrierMapper;
import org.amenal.rest.representation.FichePresentation;
import org.amenal.rest.representation.OuvrierPresentation;
import org.amenal.rest.representation.ProjetPresentation;
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
@RequestMapping("/projets")
@CrossOrigin(origins = "*")

public class ProjetController {

	@Autowired
	ProjetMetier projetMetier;

	@Autowired
	FournisseurMetier fournisseurMetier;

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN') OR"
			+ " @authoritiesService.hasAuthority('USER') OR"
			+ " @authoritiesService.hasAuthority('VISITEUR')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ProjetPresentation> getListProjet() {
		return projetMetier.ListProjet();
	}

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Void> addProjet(@Valid @RequestBody ProjetCommande p_cmd) throws URISyntaxException {

		Projet projet = projetMetier.addProjet(p_cmd);
		return ResponseEntity.created(new URI("/projet/".concat(projet.getId().toString()))).build();
	}
	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> EditProjet(@Valid @RequestBody ProjetCommande p_cmd, @PathVariable Integer id)
			throws URISyntaxException {

		Projet projet = projetMetier.modifierProjet(p_cmd, id);
		return ResponseEntity.ok().build();
	}
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'ADMIN')")
	@RequestMapping(value = "{idProjet}/fournisseur/{idFournisseur}/materiel/{idMat}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addMaterielFournisseurToProjet(
			@PathVariable(name = "idFournisseur") Integer idFournisseur,
			@PathVariable(name = "idProjet") Integer idProjet, @PathVariable(name = "idMat") Integer idMat)
			throws URISyntaxException {
		
		fournisseurMetier.AssosierMaterielToFournisseurToProjet(idFournisseur, idMat, idProjet);
		return ResponseEntity.ok().build();
	}
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'ADMIN')")
	@RequestMapping(value = "{idProjet}/ouvriers/{idOuvrier}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addOuvrierToProjet(@PathVariable(name = "idOuvrier") Integer idOuvrier,
			@PathVariable(name = "idProjet") Integer idProjet) throws URISyntaxException {
		projetMetier.AssocierOuvrierProjet(idOuvrier, idProjet);
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'ADMIN') OR"
			+ " @authoritiesService.hasAuthority(#idProjet,'USER') OR"
			+ " @authoritiesService.hasAuthority(#idProjet,'VISITEUR')")
	@RequestMapping(value = "{idProjet}/fiche/{typeFiche}", method = RequestMethod.GET)
	public List<FichePresentation> getProjetFicheByType(@PathVariable(name = "idProjet") Integer idProjet,
			@PathVariable(name = "typeFiche") String typeFiche,
			@RequestParam(name = "date", required = false) LocalDate date) {

		FicheCommande ficheCmd = new FicheCommande();
		ficheCmd.setIdProjet(idProjet);
		ficheCmd.setType(typeFiche);
		ficheCmd.setDate(date);
		return projetMetier.GetFicherByProjet(ficheCmd);

	}
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet,'USER')")
	@RequestMapping(value = "{idProjet}/fiches/{idFiche}/ouvriers", method = RequestMethod.GET)
	public List<OuvrierPresentation> findOuvriersByProjet(@PathVariable(name = "idProjet") Integer idProjet,
			@PathVariable(name = "idFiche") Integer idFiche) throws URISyntaxException {

		return projetMetier.listerOuvrierByProjet(idProjet, idFiche);

	}
	
	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteProjet(Integer id) {
		 projetMetier.DeleteProjet(id);
	}

}
