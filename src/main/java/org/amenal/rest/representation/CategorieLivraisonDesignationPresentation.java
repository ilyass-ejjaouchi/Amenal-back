package org.amenal.rest.representation;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategorieLivraisonDesignationPresentation {
	
	private String categorie;
	private List<LivraisonDesignationPresentation> livraisonDesignationPresentations = new ArrayList<LivraisonDesignationPresentation>();

}
