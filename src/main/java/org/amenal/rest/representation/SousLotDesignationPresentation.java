package org.amenal.rest.representation;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SousLotDesignationPresentation {

	private Integer id;

	private String designation;
	
	private Integer slotid;

	private String unite;

	private Double avancement;

	private List<EntreeDesignationPresentation> entreeDesignationPresentations;
	private List<EntreeDesignationNonAssoPresentation> entreeDesignationNonAssoPresentations;


}
