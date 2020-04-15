package org.amenal.rest.representation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockDesignationPresentation {

	private String designation;
	private Integer articleId;
	private String unite;
	private String BesionType;
	private Double quantite;
	private Boolean stockable;


}
