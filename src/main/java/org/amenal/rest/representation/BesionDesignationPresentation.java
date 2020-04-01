package org.amenal.rest.representation;

import java.time.LocalDate;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BesionDesignationPresentation extends DesignationPresentation {

	private String designation;
	private String unite;
	private Double quantite;
	private LocalDate dateDemande;
	private LocalDate datePrevu;
	private Boolean satisfaction;
	private String observation;

}
