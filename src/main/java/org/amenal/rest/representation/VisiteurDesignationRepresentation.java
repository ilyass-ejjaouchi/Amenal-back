package org.amenal.rest.representation;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class VisiteurDesignationRepresentation extends DesignationPresentation {

	String nom;
	String organisme;
	String objet;
	LocalDate depart;
	LocalDate arivee;
	Integer visiteurId;

}
