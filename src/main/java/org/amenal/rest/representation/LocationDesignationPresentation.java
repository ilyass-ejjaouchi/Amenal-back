package org.amenal.rest.representation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDesignationPresentation extends DesignationPresentation{

	private String libelle;

	private String unite;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double quantite;
	private String fournisseurNom;
	private String observation;


}
