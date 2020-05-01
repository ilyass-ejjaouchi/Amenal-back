package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.amenal.metier.DestinationMetier;
import org.amenal.rest.representation.DestinationRepresentation;
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
@RequestMapping("/destination")
@CrossOrigin("*")
public class DestinationController {

	@Autowired
	DestinationMetier destinationMetier;
	
	
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Void> AddDestination(@Valid @RequestBody String dst) throws URISyntaxException {
		destinationMetier.addDestination(dst);
		return ResponseEntity.created(new URI("/destination")).build();

	}
	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> UpdateDestination(@Valid @RequestBody String dst, @PathVariable Integer id) throws URISyntaxException{
		destinationMetier.UpdateDestination(dst, id);
		return ResponseEntity.created(new URI ("/destination")).build();
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void DeleteDestination(@PathVariable Integer id) {
		destinationMetier.DeleteDestination(id);
	}

	@PreAuthorize("@authoritiesService.hasAuthority('ADMIN') OR @authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/{projetId}", method = RequestMethod.GET)
	public List<DestinationRepresentation> ListerDestination(@PathVariable Integer projetId) throws URISyntaxException {
		return destinationMetier.ListerDestination(projetId);
	}

	@PreAuthorize("@authoritiesService.hasAuthority('USER')")
	@RequestMapping(value = "/projet/{projetId}", method = RequestMethod.GET)
	public List<DestinationRepresentation> ListerDestinationAssoToProjet(@PathVariable Integer projetId) {
		return destinationMetier.ListerDestinationAssoToProjet(projetId);
	}

	@PreAuthorize("@authoritiesService.hasAuthority(#projetId,'ADMIN')")
	@RequestMapping(value = "/{dstId}/projet/{projetId}", method = RequestMethod.PUT)
	public void AssoDestToProjet(@PathVariable Integer projetId, @PathVariable Integer dstId) {
		destinationMetier.AssoDestToProjet(projetId, dstId);
	}

}
