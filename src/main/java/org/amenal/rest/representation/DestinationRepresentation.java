package org.amenal.rest.representation;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DestinationRepresentation {
	
	private Integer id;
	private String destination;
	private Boolean isAsso;

}
