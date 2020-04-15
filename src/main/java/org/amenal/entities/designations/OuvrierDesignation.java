package org.amenal.entities.designations;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.fiches.OuvrierFiche;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("ODesignation")
@Setter@Getter
@NoArgsConstructor

public class OuvrierDesignation extends Designation implements Serializable {

	

	@Override
	public String toString() {
		return "OuvrierDesignation [cin=" + cin + " ]";
	}

	private static final long serialVersionUID = 1L;
	private Double hSup;
	private Double jour;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private String cin;
	private String nom;
	private String qualification;
	
	private Double travail;
	@ManyToOne
	private Ouvrier ouvrier;

	private Boolean epi;

	@ManyToOne()
	private OuvrierFiche OuvrierFiche;
	
	@ManyToOne()
	ReceptionDesignation receptionDesignationOuv;
	
	
	
	

}
