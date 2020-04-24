package org.amenal.rest.commande;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VisiteurDesignationCommande {

	String nom;
	String organisme;
	String objet;
	LocalDate depart;
	LocalDate arivee;

	Integer visiteurId;

	Integer idFiche;

}
