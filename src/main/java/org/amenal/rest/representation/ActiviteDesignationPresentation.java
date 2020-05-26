package org.amenal.rest.representation;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActiviteDesignationPresentation extends DesignationPresentation{

	private Integer id;
	private String lot;
	private List<SousLotDesignationPresentation> sousLotDesignationPresentations;

}
