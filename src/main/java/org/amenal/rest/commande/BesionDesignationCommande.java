package org.amenal.rest.commande;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BesionDesignationCommande {
	
	private Integer ArticleId;
	private Double quantite;
	private LocalDate dateDemande;
	private LocalDate datePrevu;
	private Boolean satisfaction;
	private String observation;
	private Integer idFiche;

}
