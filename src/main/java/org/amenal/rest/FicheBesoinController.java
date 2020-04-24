package org.amenal.rest;

import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.BesoinFicheMetier;
import org.amenal.rest.commande.BesoinDesignationCommande;
import org.amenal.rest.representation.BesoinRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/besoins")
@CrossOrigin("*")
public class FicheBesoinController {

	@Autowired
	BesoinFicheMetier BesoinFicheMetier;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<BesoinRepresentation> getListBesoin() {
		return BesoinFicheMetier.listeBesion();

	}

	@RequestMapping(value = "/designations", method = RequestMethod.POST)
	public void addBesoinDesignation(@Valid @RequestBody BesoinDesignationCommande bsCmd) {
		BesoinFicheMetier.addLigneDesignationBesion(bsCmd);
	}

	@RequestMapping(value = "/designations/{id}", method = RequestMethod.PUT)
	public void UpdateBesoinDesignation(@PathVariable Integer id, @Valid @RequestBody BesoinDesignationCommande bsCmd) {
		BesoinFicheMetier.updateLigneDesignationBesion(bsCmd, id);
	}

	@RequestMapping(value = "/designations/{id}", method = RequestMethod.DELETE)
	public void UpdateBesoinDesignation(@PathVariable Integer id) {
		BesoinFicheMetier.DeleteLigneDesignationBesion(id);
	}

}
