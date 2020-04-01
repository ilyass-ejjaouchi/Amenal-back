package org.amenal.entities.designations;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.Unite;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.ReceptionFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("RecDesignation")
@Getter
@Setter
@NoArgsConstructor
public class ReceptionDesignation extends Designation {

	private Integer id;
	private String libelle;
	private String unitee;
	private  String categorie; 
	private Double quantite;
	private String fournisseurNom;
	private String observation;

	@OneToOne
	private Article article;

	@ManyToOne
	private Fournisseur recFournisseur;
	
	@ManyToOne
	private ReceptionFiche receptionfiche;

	@Override
	public String toString() {
		return "ReceptionDesignation [id=" + id + ", libelle=" + libelle + ", unite=" + unitee + "]";
	}
	
	

}
