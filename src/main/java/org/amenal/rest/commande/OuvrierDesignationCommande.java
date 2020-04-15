package org.amenal.rest.commande;


import java.time.LocalTime;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class OuvrierDesignationCommande   {
	
	Integer idOuvrier;
	Integer idFiche;
	private String cin;
	private String nom;
	private String prenom;
	private String qualification;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double travail;
	private Double jour;
	private Double hSup;
	private Boolean epi;
	private Boolean valid;


}
