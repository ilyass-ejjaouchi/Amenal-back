package org.amenal.rest.commande;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class UserRoleProjetCommande {

	private String username;
	private Integer pid;
	private String role;

	public void setRole(String role) {
		this.role = role.toUpperCase();
	}

}
