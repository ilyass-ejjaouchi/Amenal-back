package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class MaterielPresentation {

	private Integer id;

	private String designation;

	private String unite;
	
	private Boolean isAssoWithProjet;
}
