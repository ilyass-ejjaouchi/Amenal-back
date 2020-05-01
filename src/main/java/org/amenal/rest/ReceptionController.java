package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.ReceptionFicheMetier;
import org.amenal.rest.commande.FournisseurCommande;
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
@RequestMapping("/reception")
@CrossOrigin(origins = "*")
public class ReceptionController {

	@Autowired
	private ReceptionFicheMetier receptionFicheMetier;

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/fournisseurs", method = RequestMethod.GET)
	public List<FournisseurPresentation> getFournisseurNotAsso() throws URISyntaxException {
		return receptionFicheMetier.ListerFournisseurNotAsso();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}", method = RequestMethod.POST)

	public ResponseEntity<Void> addFournisseur(@PathVariable Integer idFr,
			@Valid @RequestBody FournisseurCommande frCmd) throws URISyntaxException {
		receptionFicheMetier.addFournisseur(idFr, frCmd);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}/articles/{idArt}", method = RequestMethod.PUT)
	public ResponseEntity<Void> assoArticleToFournisseur(@PathVariable Integer idFr, @PathVariable Integer idArt)
			throws URISyntaxException {
		receptionFicheMetier.assoArticleToFournisseur(idFr, idArt);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}/categories/{idCat}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> DeAssoCategorieWithFournisseur(@PathVariable Integer idFr, @PathVariable Integer idCat)
			throws URISyntaxException {
		receptionFicheMetier.DeAssoCategorieWithFournisseur(idFr, idCat);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}/articles/{idArt}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> DeAssoArticleWithFournisseur(@PathVariable Integer idFr, @PathVariable Integer idArt)
			throws URISyntaxException {
		receptionFicheMetier.DeAssoArticleWithFournisseur(idFr, idArt);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority(#projetId , 'ADMIN')")
	@RequestMapping(value = "/fournisseurs/{projetId}", method = RequestMethod.GET)
	public List<FournisseurArticlePresentation> getFounrnisseurWithCategieAndArticleByProjet(
			@PathVariable Integer projetId) throws URISyntaxException {
		return receptionFicheMetier.ListerFournisseur(projetId);
	}

	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet , 'ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}/articles/{idArt}/projets/{idProjet}", method = RequestMethod.PUT)
	public ResponseEntity<Void> assoArticleFournisseurToProjet(@PathVariable Integer idProjet,
			@PathVariable Integer idFr, @PathVariable Integer idArt) throws URISyntaxException {
		receptionFicheMetier.assoArticleFournisseurToProjet(idProjet, idArt, idFr);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}
	
	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet , 'ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}/categories/{idCat}/projets/{idProjet}", method = RequestMethod.PUT)
	public ResponseEntity<Void> assoCategorieFournisseurToProjet(@PathVariable Integer idProjet,
			@PathVariable Integer idFr, @PathVariable Integer idCat) throws URISyntaxException {
		receptionFicheMetier.assoCategorieFournisseurToProjet(idProjet, idFr, idCat);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority(#idProjet , 'ADMIN')")
	@RequestMapping(value = "/fournisseurs/{idFr}/projets/{idProjet}", method = RequestMethod.PUT)
	public ResponseEntity<Void> assoFournisseurToProjet(@PathVariable Integer idProjet, @PathVariable Integer idFr)
			throws URISyntaxException {
		receptionFicheMetier.assoFournisseurToProjet(idProjet, idFr);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

}
