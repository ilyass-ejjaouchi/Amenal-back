package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.amenal.metier.ReceptionFicheMetier;
import org.amenal.rest.representation.FournisseurArticlePresentation;
import org.amenal.rest.representation.FournisseurPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reception")
@CrossOrigin(origins = "*")
public class ReceptionController {

	@Autowired
	private ReceptionFicheMetier receptionFicheMetier;
	

	@RequestMapping(value = "/fournisseurs", method = RequestMethod.GET)
	public List<FournisseurPresentation> getFournisseurNotAsso() throws URISyntaxException {
		return receptionFicheMetier.ListerFournisseurNotAsso();
	}

	@RequestMapping(value = "/fournisseurs/{idFr}", method = RequestMethod.POST)

	public ResponseEntity<Void> addFournisseur(@PathVariable Integer idFr) throws URISyntaxException {
		receptionFicheMetier.addFournisseur(idFr);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@RequestMapping(value = "/fournisseurs/{idFr}/articles/{idArt}", method = RequestMethod.PUT)
	public ResponseEntity<Void> assoArticleToFournisseur(@PathVariable Integer idFr, @PathVariable Integer idArt)
			throws URISyntaxException {
		receptionFicheMetier.assoArticleToFournisseur(idFr, idArt);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}

	@RequestMapping(value = "/fournisseurs/{projetId}", method = RequestMethod.GET)
	public List<FournisseurArticlePresentation> getFounrnisseurWithCategieAndArticleByProjet(
			@PathVariable Integer projetId) throws URISyntaxException {
		return receptionFicheMetier.ListerFournisseur(projetId);
	}

	@RequestMapping(value = "/fournisseurs/{idFr}/articles/{idArt}/projets/{idProjet}", method = RequestMethod.PUT)
	public ResponseEntity<Void> assoArticleFournisseurToProjet(@PathVariable Integer idProjet,
			@PathVariable Integer idFr, @PathVariable Integer idArt) throws URISyntaxException {
		receptionFicheMetier.assoArticleFournisseurToProjet(idProjet,idArt, idFr);
		return ResponseEntity.created(new URI("reception/fournisseurs/")).build();
	}


	
}
