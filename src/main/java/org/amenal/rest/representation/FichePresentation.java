package org.amenal.rest.representation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.amenal.entities.designations.Stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class FichePresentation {
	
	private String type;
	
	protected Integer id;
	
	

	private Integer count;
	
	private LocalDate date;
	
	private Boolean isValidated = false;
	
	private List<DesignationPresentation> designations;
	
	private List<CategorieReceptionDesignationPresentation> categories;
	
	private List<StockPresentation> stockDesignations;
	
	private List<CategorieLivraisonDesignationPresentation> categorieLivraisons;
	
	
	
	
	

}
