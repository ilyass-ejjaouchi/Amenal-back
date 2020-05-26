package org.amenal.rest.representation;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LotPresentation {
	
	Integer id;
	String designation;
	Boolean isAsso;
	List<SousLotPresentation> sousLots =
			new ArrayList<SousLotPresentation>() ;

}
