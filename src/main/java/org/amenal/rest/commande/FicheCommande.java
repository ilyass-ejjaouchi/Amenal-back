package org.amenal.rest.commande;

import java.time.LocalDate;
import java.util.Date;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class FicheCommande {
	
	Integer idProjet;
	LocalDate date;
	String type;

}
