package org.amenal.rest.representation;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class OuvrierDesignationPresentation {
	
	private String cin;
	private String nom;
	private String prenom;
	private String qualification;
	private String tempsDebut;
	private String tempsFin;
	private Double hSup;
	private Double jour;
	private Boolean epi;

}
