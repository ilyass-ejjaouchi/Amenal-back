package org.amenal.entities.designations;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.amenal.entities.Article;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.fiches.BesionFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("BsnDesignation")
@Setter
@Getter
@NoArgsConstructor
public class BesionDesignation extends Designation {

	private String designation;
	private String unite;
	
	
	private Double quantite;
	private LocalDate dateDemande;
	private LocalDate datePrevu;
	private Boolean satisfaction;
	private String observation;
	
	@OneToOne
	private Article article;
	
	@OneToOne
	private QualificationOuvrier qualification;

	@ManyToOne
	private BesionFiche besionFiche;

}
