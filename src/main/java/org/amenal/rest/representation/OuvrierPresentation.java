package org.amenal.rest.representation;

import java.util.Date;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.fiches.OuvrierFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor

public class OuvrierPresentation {

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
}
