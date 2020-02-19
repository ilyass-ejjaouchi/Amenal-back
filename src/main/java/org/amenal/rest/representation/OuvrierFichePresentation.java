package org.amenal.rest.representation;

import java.util.Date;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class OuvrierFichePresentation {
	
	protected Integer id;

	private Integer count;
	
	private Date date;
	
	private Boolean isValidated = false;
	
	private List<OuvrierDesignationPresentation> ouvrierDesignations;
	
	
	

}
