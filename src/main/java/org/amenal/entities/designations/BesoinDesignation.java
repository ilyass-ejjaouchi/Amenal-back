package org.amenal.entities.designations;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.amenal.entities.Article;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.fiches.BesoinFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("BsnDesignation")
@Setter
@Getter
@NoArgsConstructor
public class BesoinDesignation extends Designation {


	private String designation;
	private String unite;
	
	
	private Double quantite;
	private LocalDate dateDemande;
	private LocalDate datePrevu;
	private Boolean satisfaction;
	private Integer retard;
	private String observation;
	
	@OneToOne
	private Article article;
	
	@OneToOne
	private QualificationOuvrier qualification;

	@ManyToOne
	private BesoinFiche besoinFiche;

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation.toUpperCase();
	}

	public String getUnite() {
		return unite;
	}

	public void setUnite(String unite) {
		this.unite = unite.toUpperCase();
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation.toUpperCase();
	}
	

}
