package org.amenal.rest;

import java.util.List;

import org.amenal.metier.FicheVisiteurMetier;
import org.amenal.rest.commande.VisiteurDesignationCommande;
import org.amenal.rest.representation.VisiteurPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations/visiteurs")
@CrossOrigin(origins = "*")
public class VisiteurDesignationController {
	@Autowired
	FicheVisiteurMetier ficheVisiteurMetier;
	
	@PreAuthorize(" @authoritiesService.hasAuthorityFiche(#pid ,'USER')")
	@RequestMapping(value = "/projets/{pid}/visiteurs", method = RequestMethod.GET)
	List<VisiteurPresentation> listerVisiteurAssoToProjet(@PathVariable Integer pid) {
		return ficheVisiteurMetier.listerVisiteur(pid);
	}

	@PreAuthorize(" @authoritiesService.hasAuthorityFiche(#vstCmd.idFiche ,'USER')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	void ajouterVisiteurDesignation(@RequestBody VisiteurDesignationCommande vstCmd) {
		ficheVisiteurMetier.addVisiteurDesignation(vstCmd);
	}

	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#id ,'USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	void ajouterVisiteurDesignation(@RequestBody VisiteurDesignationCommande vstCmd, @PathVariable Integer id) {
		ficheVisiteurMetier.updateVisiteurDesignation(vstCmd, id);
	}

	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#id ,'USER')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void deleteVisiteurDesignation(@PathVariable Integer id) {
		ficheVisiteurMetier.deleteVisiteurDesignation(id);
	}

}
