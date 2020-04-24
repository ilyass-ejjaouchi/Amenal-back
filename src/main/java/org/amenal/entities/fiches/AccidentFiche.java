package org.amenal.entities.fiches;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.AccidentDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("accident")
@Setter
@Getter
@NoArgsConstructor
public class AccidentFiche  extends Fiche implements Serializable{
	
	private String type = FicheTypeEnum.ACC.getCode();

	private String alpha = "h";

	
	
	@OneToMany(mappedBy = "accidentFiche" , cascade = CascadeType.ALL)
	private List<AccidentDesignation> accDesignations = new ArrayList<AccidentDesignation>();

}
