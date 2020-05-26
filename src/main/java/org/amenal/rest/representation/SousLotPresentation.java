package org.amenal.rest.representation;

import java.util.ArrayList;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SousLotPresentation {

	Integer id;
	String designation;
	String unite;
	Boolean isAsso;
	List<EntreePresentation> entrees =
			new ArrayList<EntreePresentation>();
}
