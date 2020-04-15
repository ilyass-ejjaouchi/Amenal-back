package org.amenal.rest.representation;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class ReceptionDesignationPresentation {

	private Integer id;
	private String designation;
	private String unite;
	private Double quantite = 0.0;
	private String fournisseurNom;
	private String observation;
	private Boolean valid;


}
