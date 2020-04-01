package org.amenal.entities.designations;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.amenal.entities.Article;
import org.amenal.entities.fiches.BesionFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Designation")
@Entity
@DiscriminatorValue("BsnDesignation")
@Setter
@Getter
@NoArgsConstructor
public class BesionDesignation extends Designation {

	private String designation;
	private String unite;
	
	@OneToOne
	private Article article;
	private Double quantite;
	private LocalDate dateDemande;
	private LocalDate datePrevu;
	private Boolean satisfaction;
	private String observation;

	@ManyToOne
	private BesionFiche besionFiche;

}
