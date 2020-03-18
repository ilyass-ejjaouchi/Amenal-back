package org.amenal.rest.representation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class FichePresentation {
	
	protected Integer id;

	private Integer count;
	
	private LocalDate date;
	
	private Boolean isValidated = false;
	
	private List<DesignationPresentation> designations;
	
	
	

}
