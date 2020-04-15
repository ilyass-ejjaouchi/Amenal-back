package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocDesignationRepresentation  extends DesignationPresentation{
	
	private String intitule;
	private Boolean disponibilite;
}
