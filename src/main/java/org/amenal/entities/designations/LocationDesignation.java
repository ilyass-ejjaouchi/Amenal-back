package org.amenal.entities.designations;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.amenal.entities.fiches.LocationFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("LDesignation")
@Getter
@Setter
@NoArgsConstructor
public class LocationDesignation extends Designation {
	private static final long serialVersionUID = 1L;

	private String libelle;

	private String unite;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double quantite;
	private String fournisseurNom;
	private String observation;

	@ManyToOne
	private LocationFiche locationFiche;

}
