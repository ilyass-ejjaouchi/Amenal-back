package org.amenal.rest.security;

import java.util.List;

import org.amenal.metier.security.AccountMetier;
import org.amenal.rest.commande.RegistrationForm;
import org.amenal.rest.commande.UserRoleProjetCommande;
import org.amenal.rest.representation.UserPresentation;
import org.amenal.rest.representation.UserProjetRolePresentation;
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
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {
	@Autowired
	private AccountMetier accountService;

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void addUser(@RequestBody RegistrationForm data) {
		accountService.AddUser(data);
	}

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/role", method = RequestMethod.POST)
	public void grantRoleOfProjetToUser(@RequestBody UserRoleProjetCommande cmd) {
		accountService.grantRoleOfProjetToUser(cmd);
	}
	
	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public List<UserProjetRolePresentation> ListerUserWithProjet() {
		return accountService.ListerUserWithProjet();
	}

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/role/{assId}", method = RequestMethod.PUT)
	public void UpdategrantedRoleOfProjetToUser(@RequestBody UserRoleProjetCommande cmd, @PathVariable Integer assId) {
		accountService.UpdategrantedRoleOfProjetToUser(cmd, assId);
	}

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/role/{assId}", method = RequestMethod.DELETE)
	public void DeletegrantedRoleOfProjetToUser(@PathVariable Integer assId) {
		accountService.DeletegrantedRoleOfProjetToUser(assId);
	}

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<UserPresentation> ListUser() {
		return this.accountService.ListUser();
	}

	@PreAuthorize("@authoritiesService.isRoot()")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> SupprimerUser(@PathVariable Integer id) {
		this.accountService.supprimerUser(id);
		return ResponseEntity.ok().build();

	}

}
