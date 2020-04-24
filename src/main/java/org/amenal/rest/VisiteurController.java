package org.amenal.rest;

import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.FicheVisiteurMetier;
import org.amenal.rest.commande.VisiteurCommande;
import org.amenal.rest.representation.VisiteurPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visiteurs")
@CrossOrigin(origins = "*")
public class VisiteurController {

	@Autowired
	FicheVisiteurMetier ficheVisiteurMetier;

	@RequestMapping(value = "/projets/{pid}", method = RequestMethod.GET)
	List<VisiteurPresentation> listerVisiteur(@PathVariable Integer pid) {
		return ficheVisiteurMetier.listerVisiteur(pid);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	void ajouterVisiteur(@Valid @RequestBody VisiteurCommande vsCmd) {
		ficheVisiteurMetier.addVisteur(vsCmd);
	}
	
	@RequestMapping(value = "/{vid}/projets/{pid}", method = RequestMethod.PUT)
	void assoVisiteurToProjet(@PathVariable Integer pid,@PathVariable Integer vid) {
		ficheVisiteurMetier.AssosierVisiteurToProjet(pid, vid);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	void modifierVisiteur(@Valid @RequestBody VisiteurCommande vsCmd, @PathVariable Integer id) {
		ficheVisiteurMetier.updateVisteur(vsCmd, id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	void suprimerVisiteur(@PathVariable Integer id) {
		ficheVisiteurMetier.deleteVisteur(id);
	}

}
