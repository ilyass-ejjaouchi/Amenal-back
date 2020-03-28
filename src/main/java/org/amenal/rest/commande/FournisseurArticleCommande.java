package org.amenal.rest.commande;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class FournisseurArticleCommande {
	
	private Integer id;
	private String 	fournisseurNom;
	private Boolean isAssoWithProjet;
	private List<CategorieArticleCommande> categories;

}
