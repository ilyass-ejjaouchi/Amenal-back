package org.amenal.rest;

import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.DocumentFicheMetier;
import org.amenal.rest.representation.DocumentRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
@CrossOrigin("*")
public class DocumentController {

	@Autowired
	DocumentFicheMetier documentFicheMetier;

	@PreAuthorize(" @authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void addDocument(@Valid @RequestBody String intitule) throws URISyntaxException {
		this.documentFicheMetier.AddDocument(intitule);
	}

	@PreAuthorize(" @authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/projets/{pid}", method = RequestMethod.GET)
	public List<DocumentRepresentation> ListerDocument(@PathVariable Integer pid) throws URISyntaxException {
		return this.documentFicheMetier.ListerDocument(pid);
	}

	@PreAuthorize("@authoritiesService.hasAuthority(#pid,'ADMIN')")
	@RequestMapping(value = "/{did}/projets/{pid}", method = RequestMethod.PUT)
	public void AssoDocumentToProjet(@PathVariable Integer pid, @PathVariable Integer did) throws URISyntaxException {
		this.documentFicheMetier.AssoDocumentToProjet(pid, did);
	}
	
	@PreAuthorize(" @authoritiesService.hasAuthorityDs(#did ,'USER')")
	@RequestMapping(value = "/designation/{did}", method = RequestMethod.PUT)
	public void updateDocumentDesignation( @PathVariable Integer did) throws URISyntaxException {
		this.documentFicheMetier.updateDocumentDesignationDisponibilite( did);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/{did}", method = RequestMethod.PUT)
	public void updateDocument( @Valid @RequestBody String intitule,@PathVariable Integer did) throws URISyntaxException {
		this.documentFicheMetier.UpdateDocument(intitule, did);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/{did}", method = RequestMethod.DELETE)
	public void deleteDocument( @PathVariable Integer did) throws URISyntaxException {
		this.documentFicheMetier.DeleteDocument(did);
	}

}
