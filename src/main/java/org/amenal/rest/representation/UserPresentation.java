package org.amenal.rest.representation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPresentation {

	Integer id;
	String username;
	String password;
	List<ProjetUserPresentation>  projets;
	Boolean isRoot;

}
