package org.amenal.entities.designations;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.amenal.entities.Article;
import org.amenal.entities.Destination;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.LivraisonFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("LivDesignation")
@Getter
@Setter
@NoArgsConstructor
public class LivraisonDesignation extends Designation {
	
	private String designation;
	private String unite;
	private Double quantite;
	private String categorieLv;
	@ManyToOne
	private Destination destination;
	private String destinationNom;
	@ManyToOne
	private Article articleLvr;
	
	private String observation;
	

	
}
