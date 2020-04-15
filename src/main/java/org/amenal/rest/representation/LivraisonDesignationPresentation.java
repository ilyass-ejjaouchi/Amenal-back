package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LivraisonDesignationPresentation extends DesignationPresentation {
	
	private String designation;
	private Integer articleId;
	private String unite;
	private String destinationNom;
	private Integer destinationId;
	private Double quantite;
	private String observation;

}
