package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.amenal.entities.Projet;
import org.amenal.metier.ProjetMetier;
import org.amenal.rest.commande.ProjetCommande;
import org.amenal.rest.mapper.ProjetMapper;
import org.amenal.rest.representation.ProjetPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projets")
public class ProjetController {
	
	@Autowired
	ProjetMetier projetMetier;
	@Autowired
	ProjetMapper projetMapper;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ProjetPresentation> getListProjet() {
		return projetMetier.ListProjet().stream().map(o -> projetMapper.toRepresentation(o)).collect(Collectors.toList());
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public  ResponseEntity<Void> addProjet(@Valid @RequestBody ProjetCommande p_cmd) throws URISyntaxException{
		
		
		Projet projet = projetMapper.toEntity(p_cmd);

		projet =  projetMetier.addProjet(projet , p_cmd.getFichierTypes());
			return ResponseEntity.created(new URI("/projet/".concat(projet.getId().toString()))).build();
	}


}
