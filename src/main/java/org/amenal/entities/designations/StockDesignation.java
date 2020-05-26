package org.amenal.entities.designations;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.amenal.entities.Article;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.fiches.Fiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StockDesignation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String designation;
	private String unite;
	private Double quantite = 0.0;
	
	
	private Boolean stockable;
	
	@OneToOne(cascade=CascadeType.DETACH)
	QualificationOuvrier qualifOuvrier;
	
	@OneToOne(cascade=CascadeType.DETACH)
	Article article;
	
	@Transient
	String entreeType;
	
	@ManyToOne(cascade= CascadeType.ALL)
	private Stock stock;
	
	
	Fiche getFiche() {
		// TODO Auto-generated method stub
		return stock.getStockFiche();
	}
	

}
