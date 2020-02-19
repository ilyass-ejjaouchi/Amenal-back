package org.amenal.rest.representation;

import java.util.ArrayList;
import java.util.List;

import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.FicheTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ProjetPresentation {
	
	Integer Id;
	
	private String titre;
	
	private List<String> fichierTypes = new ArrayList<String>();
	

}
