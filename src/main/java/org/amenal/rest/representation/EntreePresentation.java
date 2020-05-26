package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EntreePresentation {

	Integer id;
	String unite;
	String entreeNom;
	Integer idEntree;
	String type;
	Double quantiteEstimer;

}
