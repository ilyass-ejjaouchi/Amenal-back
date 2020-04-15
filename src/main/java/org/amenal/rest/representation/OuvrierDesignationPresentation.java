package org.amenal.rest.representation;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class OuvrierDesignationPresentation extends DesignationPresentation {

	Integer idOuvrier;
	private String cin;
	private String nom;
	private String prenom;
	private String qualification;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double hSup;
	private Double jour;
	private Boolean epi;


}
