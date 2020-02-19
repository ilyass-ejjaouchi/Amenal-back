package org.amenal.rest.commande;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class OuvrierDesignationCommande   {
	
	Integer idOuvrier;
	Integer idFiche;
	private String tempsDebut;
	private String tempsFin;
	private Double jour;
	private Double hSup;
	private Boolean epi;
	

}
