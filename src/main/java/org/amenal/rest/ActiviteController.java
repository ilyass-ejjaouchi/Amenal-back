package org.amenal.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.amenal.dao.ProjetRepository;
import org.amenal.metier.FicheActiviteMetier;
import org.amenal.rest.commande.EntreeDesignationCommande;
import org.amenal.rest.representation.EntreeDesignationNonAssoPresentation;
import org.amenal.rest.representation.LotAssoPresentation;
import org.amenal.rest.representation.SousLotDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activite")
@CrossOrigin("*")
public class ActiviteController {

	@Autowired
	FicheActiviteMetier ficheActiviteMetier;

	@RequestMapping(value = "/designation/{dsId}", method = RequestMethod.DELETE)
	void DeleteDesignation(@PathVariable Integer dsId) {
		ficheActiviteMetier.DeleteDesignation(dsId);
	}

	@RequestMapping(value = "/entree/{id}", method = RequestMethod.DELETE)
	void deleteEntreeDesignation(@PathVariable Integer id) {
		this.ficheActiviteMetier.deleteEntreeDesignation(id);
	}

	@RequestMapping(value = "/entree/{id}", method = RequestMethod.PUT)
	void UpdateEntreeDesignation(@PathVariable Integer id, @RequestBody EntreeDesignationCommande entreeDsCmd) {
		this.ficheActiviteMetier.UpdateEntreeDesignation(entreeDsCmd, id);
	}
	
	
	@RequestMapping(value = "/entree/sousLot/projet/{projetId}", method = RequestMethod.GET)
	List<SousLotDesignationPresentation> listEntreeDesignationNoAsso(@PathVariable Integer projetId,
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return ficheActiviteMetier.listEntreeDesignationNoAsso(projetId, date);
	}
	
	@RequestMapping(value = "/sousLot/{id}/avancement/{av}", method = RequestMethod.PUT)
	void updateSousLotDesignation(@PathVariable Integer id,
			@PathVariable Double av) {
		 ficheActiviteMetier.updateSousLotDesignation(id, av);
	}

	@RequestMapping(value = "/lot/projet/{projetId}/fiche/{fid}", method = RequestMethod.GET)
	List<LotAssoPresentation> listLotParProjet(@PathVariable Integer projetId, @PathVariable Integer fid) {
		return ficheActiviteMetier.listLotParProjet(projetId, fid);
	}

	@RequestMapping(value = "/lot/{lotId}/fiche/{ficheId}/projet/{projetId}", method = RequestMethod.POST)
	void addActiviteDesignation(@PathVariable Integer projetId, @PathVariable Integer lotId,
			@PathVariable Integer ficheId) {
		ficheActiviteMetier.addActiviteDesignation(lotId, ficheId, projetId);
	}

	@RequestMapping(value = "/sousLot/{sLotDsId}", method = RequestMethod.POST)
	void addEntreeToSousLotDesignation(@PathVariable Integer sLotDsId,
			@RequestBody EntreeDesignationCommande entreeDsCmd) {

		ficheActiviteMetier.addEntreeToSousLotDesignation(sLotDsId, entreeDsCmd);
	}

}
