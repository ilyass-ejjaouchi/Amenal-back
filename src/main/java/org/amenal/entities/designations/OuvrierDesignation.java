package org.amenal.entities.designations;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.fiches.OuvrierFiche;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DiscriminatorValue("ODesignation")
@Getter @Setter @NoArgsConstructor

public class OuvrierDesignation extends Designation {
	
	private static final long serialVersionUID = 1L;
	private Double hSup;
	private Double jour;
	private String tempsDebut;
	private String tempsFin;
	private String cin;
	private String nom;
	private String qualification;
	@ManyToOne
	private Ouvrier ouvrier;
	@ManyToOne
	private OuvrierFiche ouvrieFichier;
	private Boolean epi;
	
	 

	
}
