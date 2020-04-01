package org.amenal.rest.representation;

import java.util.ArrayList;
import java.util.List;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockPresentation {

	private String categorie;

	private List<StockDesignationPresentation> stockDesignations = 
			new ArrayList<StockDesignationPresentation>();

}
