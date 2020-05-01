package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.entities.Ouvrier;
import org.amenal.metier.OuvrierMetier;
import org.amenal.metier.QualificationOuvrierMetier;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.representation.OuvrierPresentation;
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
@RequestMapping("/ouvriers")
@CrossOrigin(origins = "*")
public class OuvrierController {

	@Autowired
	OuvrierMetier ouvrierMetier;

	@Autowired
	QualificationOuvrierMetier qualificationOuvrierMetier;

	@RequestMapping(value = "", method = RequestMethod.POST)
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	public ResponseEntity<Void> addOuvrier(@Valid @RequestBody OuvrierCommande ouvCmd) throws URISyntaxException {
		Ouvrier ouvrier = ouvrierMetier.ajouterOuvrier(ouvCmd);
		return ResponseEntity.created(new URI("/projet/".concat(ouvrier.getId().toString()))).build();
	}
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<OuvrierPresentation> ListeOuvrier() throws URISyntaxException {
		return ouvrierMetier.ListerOuvriers();
	}
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{OuvId}", method = RequestMethod.DELETE)
	public void SupprimerOuvrier(@PathVariable Integer OuvId) {
		ouvrierMetier.SupprimerOuvrier(OuvId);

	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")

	@RequestMapping(value = "/{OuvId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateOuvrier(@PathVariable Integer OuvId, @Valid @RequestBody OuvrierCommande ouvCmd)
			throws URISyntaxException {
		ouvrierMetier.modifierOuvrier(ouvCmd, OuvId);
		return ResponseEntity.ok().build();
	}
	
	
	/*****************************************************************/
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/qualifications", method = RequestMethod.POST)
	public ResponseEntity<Void> addQualification( @RequestBody String code) throws URISyntaxException {

		Integer id = this.qualificationOuvrierMetier.AjouterQualificationOuvrier(code);

		return ResponseEntity.created(new URI("/ouvrier/qualification".concat(id.toString()))).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/qualifications", method = RequestMethod.GET)
	public List<String> listeQualification() throws URISyntaxException {

		return qualificationOuvrierMetier.ListerQualificationOuvrier();
	}
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/qualifications/{id}", method = RequestMethod.DELETE)
	public void DeleteQualification( @PathVariable String code) throws URISyntaxException {

		 qualificationOuvrierMetier.SupprimerQualificationOuvrier(code);
	}

}
