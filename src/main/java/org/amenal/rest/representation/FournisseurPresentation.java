package org.amenal.rest.representation;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class FournisseurPresentation {

	
	private Integer id;
	private String fournisseurNom;
	MaterielPresentation materiel;
	List<MaterielPresentation> materiels;
	private Boolean isAssoWithProjet;
	public FournisseurPresentation(Integer id, String fournisseurNom, List<MaterielPresentation> materiels) {
		super();
		this.id = id;
		this.fournisseurNom = fournisseurNom;
		this.materiels = materiels;
	}
	
	
	

}
