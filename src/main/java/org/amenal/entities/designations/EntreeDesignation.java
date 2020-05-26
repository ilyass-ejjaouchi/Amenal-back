package org.amenal.entities.designations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.amenal.entities.Article;
import org.amenal.entities.Entree;
import org.amenal.entities.QualificationOuvrier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EntreeDesignation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String type;
	
	private Double quantite;
	
	private String designation;
	
	private String unite;
	
	@ManyToOne
	private Article article;
	
	@ManyToOne
	private QualificationOuvrier qualification;
	
	@ManyToOne
	private SousLotDesignation sousLotDesignation;

	public String getDesignation() {
		if(sousLotDesignation.getActiviteDesignation().getFiche().getIsValidated())
			return designation;
		else {
			if(type.equals(Entree.ARTICLE))
				return article.getDesignation();
			else if(type.equals(Entree.OUVRIER))
			return qualification.getCode();
			else return "";
		}
	}

	public String getUnite() {
		if(sousLotDesignation.getActiviteDesignation().getFiche().getIsValidated())
			return unite;
		else {
			if(type.equals(Entree.ARTICLE))
				return article.getUnite().getUnite();
			else if(type.equals(Entree.OUVRIER))
			return "H";
			else return "";
		}
	}
	
	
	
	
	
	

}
