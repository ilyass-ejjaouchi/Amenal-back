package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VisiteurPresentation {

	Integer id;
	String nom;
	String organisme;
	Boolean isAsso;

}
