package org.amenal.entities.designations;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.amenal.entities.CategorieArticle;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.rest.representation.StockDesignationPresentation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {
	
	public static final String OUVRIER ="MAIN D'OEUVRE";
	public static final String LOCATION ="LOCATION";
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String categorie ;
	
	private Boolean stockable = true;
	
	@OneToOne(cascade = CascadeType.ALL)
	 CategorieArticle artCategorie = new CategorieArticle();

	@ManyToOne
	private StockFiche stockFiche;

	@OneToMany(mappedBy = "stock" , 
			cascade = 
		{ CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE  , CascadeType.DETACH ,CascadeType.MERGE , })
	private List<StockDesignation> stockDesignations = new ArrayList<StockDesignation>();
	
	
}
