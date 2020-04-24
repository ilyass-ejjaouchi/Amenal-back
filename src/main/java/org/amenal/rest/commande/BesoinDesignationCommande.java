package org.amenal.rest.commande;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BesoinDesignationCommande {
	
	private Integer BesoinId;
	private String designation;
	private String unite;
	private String BesoinType;
	private Double quantite;
	private LocalDate dateDemande;
	private LocalDate datePrevu;
	private Integer retard;
	private Boolean satisfaction;
	private String observation;
	private Boolean valid;
	private Integer idFiche;

}
