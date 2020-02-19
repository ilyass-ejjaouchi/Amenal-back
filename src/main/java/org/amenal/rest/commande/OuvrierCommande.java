package org.amenal.rest.commande;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor

public class OuvrierCommande {
	 
	private Date dateNaissance;
	private Date dateRecrutement;
	private String tele;
	private String appreciation;
	private int nbrJours;
	private String qualification;
	private int nbFile = 0;
	private String cin;
	private String nom;
	private String prenom;
	private Integer idProjet ;
}
