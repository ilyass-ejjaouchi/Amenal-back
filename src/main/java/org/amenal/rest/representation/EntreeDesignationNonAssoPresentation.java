package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EntreeDesignationNonAssoPresentation {

	Integer id;
	String unite;
	String entreeNom;
	String type;
	Double quantite = 0.0;
	Boolean isRecomander=false;

}
