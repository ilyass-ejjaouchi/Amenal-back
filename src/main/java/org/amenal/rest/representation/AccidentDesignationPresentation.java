package org.amenal.rest.representation;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccidentDesignationPresentation extends DesignationPresentation {
	
	private Integer id;
	private String objet;
	private LocalTime heure; 

}
