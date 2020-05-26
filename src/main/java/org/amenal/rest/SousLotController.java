package org.amenal.rest;

import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.SousLotMetier;
import org.amenal.rest.commande.EntreeCommande;
import org.amenal.rest.commande.SousLotCommande;
import org.amenal.rest.representation.EntreeNoAssoPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SousLots")
@CrossOrigin(origins = "*")
public class SousLotController {

	@Autowired
	SousLotMetier sousLotMetier;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void AddSousLot(@Valid @RequestBody SousLotCommande slotcmd) {
		sousLotMetier.AddSousLot(slotcmd);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void UpdateSousLot(@Valid @RequestBody SousLotCommande slotcmd, @PathVariable Integer id) {
		sousLotMetier.UpdateSousLot(slotcmd, id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void DeleteSousLot(@PathVariable Integer id) {
		sousLotMetier.DeleteSousLot(id);
	}
	

	@RequestMapping(value = "/{slotId}/entrees", method = RequestMethod.POST)
	public void AddEntreeToSousLot(@Valid @RequestBody EntreeCommande entreeCmd, @PathVariable Integer slotId) {

		sousLotMetier.AddEntreeToSousLot(entreeCmd, slotId);
	}
	@RequestMapping(value = "/entrees/{id}", method = RequestMethod.PUT)
	public void UpdateEntreeToSousLot(@Valid @RequestBody EntreeCommande entreeCmd, @PathVariable Integer id) {

		sousLotMetier.updateEntree(entreeCmd, id);
	}
	@RequestMapping(value = "/entrees/{id}", method = RequestMethod.DELETE)
	public void DeleteEntree(@PathVariable Integer id) {
		sousLotMetier.DeleteSousLot(id);
	}
	@RequestMapping(value = "/entrees", method = RequestMethod.GET)
	public List<EntreeNoAssoPresentation> ListArticleNonAsso() {

		return sousLotMetier.listEntree();
	}
	@RequestMapping(value = "/{sousLotId}/projets/{projetId}", method = RequestMethod.PUT)
	public void AssoSousLotToProjet(@PathVariable Integer sousLotId 
			, @PathVariable Integer projetId) {

		 sousLotMetier.AssoSousLotToProjet(sousLotId, projetId);
	}

}
