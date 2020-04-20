package org.amenal.entities.designations;

import java.time.LocalTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.amenal.entities.fiches.AccidentFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@DiscriminatorValue("AccDesignation")
@Getter
@Setter
@NoArgsConstructor
public class AccidentDesignation extends Designation {
	
	private String objet;
	private LocalTime heure;
	
	@ManyToOne
	private AccidentFiche accidentFiche;

}
