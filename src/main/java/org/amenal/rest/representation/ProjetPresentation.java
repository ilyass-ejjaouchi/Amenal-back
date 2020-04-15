package org.amenal.rest.representation;

import java.time.LocalDate;
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
	
	Integer id;
	
	private String intitule;
	private String abreveation;
	private String description;
	private LocalDate debut;
	private LocalDate fin;
	
	private List<String> fichierTypes = new ArrayList<String>();
	

}
