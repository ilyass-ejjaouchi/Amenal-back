package org.amenal.rest.commande;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceptionDesignationCommande {
	
	private Double quantite;
	private String observation;
	private Integer idFiche;
	private Integer idProjet ;
	private Integer idArticle;
	private Integer idFournisseur;
	private Boolean valid;
	

}
