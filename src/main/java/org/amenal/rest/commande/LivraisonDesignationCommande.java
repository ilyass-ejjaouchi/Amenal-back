package org.amenal.rest.commande;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LivraisonDesignationCommande {

	private Integer idMateriel;
	private Integer idFiche;
	private Integer destinationId;
	private Double quantite;
	private Boolean valid;
	private String observation;

}
