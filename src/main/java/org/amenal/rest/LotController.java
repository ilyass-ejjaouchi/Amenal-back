package org.amenal.rest;

import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.LotMetier;
import org.amenal.rest.representation.LotPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lots")
@CrossOrigin("*")
public class LotController {

	@Autowired
	LotMetier lotMetier;

	@RequestMapping(value = "/projets/{projetId}", method = RequestMethod.GET)
	public List<LotPresentation> listLot(@PathVariable Integer projetId ) {
		return lotMetier.listLot(projetId);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public void addLot(@Valid @RequestBody String lotDs) {
		lotMetier.AddLot(lotDs);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void updateLot(@Valid @RequestBody String lotDs, @PathVariable Integer id) {
		lotMetier.UpdateLot(lotDs, id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteLot(@PathVariable Integer id) {
		lotMetier.DeleteLot(id);
	}
	
	@RequestMapping(value = "/{lotId}/projets/{projetId}", method = RequestMethod.PUT)
	public void AssoLotToProjet(@PathVariable Integer projetId , @PathVariable Integer lotId) {
		lotMetier.AssoLotToProjet(lotId , projetId);
	}

}
