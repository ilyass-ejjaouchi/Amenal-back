package org.amenal.entities.designations;

import java.time.LocalDate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.amenal.entities.Visiteur;
import org.amenal.entities.fiches.VisiteurFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@DiscriminatorValue("VstDesignation")
@Getter @Setter @NoArgsConstructor
public class VisiteurDesignation extends Designation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	String nom;
	String organisme;
	String objet;
	LocalDate depart;
	LocalDate arivee;
	@ManyToOne
	Visiteur visiteur;
	
	@ManyToOne
	VisiteurFiche visiteurFiche;

}
