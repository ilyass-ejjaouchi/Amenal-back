package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DocumentRepresentation {
	private Integer id;

	private String intitule;

	private Boolean isAsso;
	
}
