package org.amenal.rest.commande;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class OuvrierDesignationCommande {
	
	Integer idOuvrier;
	Integer idFiche;
	private Double hSup;
	private Double jour;
	private String tempsDebut;
	private String tempsFin;
	private String cin;
	private String nom;
	private String qualification;
	private Boolean epi;
	

}
