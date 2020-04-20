package org.amenal.rest.commande;

import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccidentDesignationCommande {

	private String objet;
	private LocalTime heure;
	private Integer ficheID;

}
