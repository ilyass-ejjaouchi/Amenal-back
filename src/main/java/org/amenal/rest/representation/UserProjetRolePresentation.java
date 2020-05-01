package org.amenal.rest.representation;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProjetRolePresentation {

	private String username;
	private List<RoleProjetPresentation> roles;

}
