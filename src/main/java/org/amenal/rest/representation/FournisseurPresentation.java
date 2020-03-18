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
	private String libelle;
	List<MaterielPresentation> materiel;
	private Boolean isAssoWithProjet;
	public FournisseurPresentation(Integer id, String libelle, List<MaterielPresentation> materiel) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.materiel = materiel;
	}
	
	
	

}
