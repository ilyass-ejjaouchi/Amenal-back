package org.amenal.rest.representation;

import java.util.Date;
import java.util.List;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor

public class OuvrierPresentation {

	Integer id;
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
	private List<Integer> idProjets;
}
