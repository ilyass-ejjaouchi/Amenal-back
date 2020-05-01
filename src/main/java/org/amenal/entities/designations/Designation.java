package org.amenal.entities.designations;


import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.fiches.AccidentFiche;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.OuvrierFiche;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_designation")
@Getter @Setter @NoArgsConstructor

public class Designation implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private int nbFile = 0;
	private Boolean valid;
	@ManyToOne
	private Fiche fiche;

	
	
	
	
	
	

}
