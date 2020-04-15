package org.amenal.entities.fiches;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import javax.persistence.OneToMany;

import org.amenal.entities.designations.OuvrierDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@DiscriminatorValue("ouvrier")
@Getter @Setter @NoArgsConstructor
public class OuvrierFiche extends Fiche implements Serializable {
	
	private String type = FicheTypeEnum.MOO.getCode();

	
	@OneToMany(mappedBy="OuvrierFiche",cascade=CascadeType.REMOVE )
	private List<OuvrierDesignation> ouvrierDesignation;
	
	
	public void addLignedesignation(OuvrierDesignation Designation) {
		// TODO Auto-generated method stub
		if (ouvrierDesignation == null) {
			ouvrierDesignation = new ArrayList<OuvrierDesignation>();
		}
		ouvrierDesignation.add(Designation);
		Designation.setOuvrierFiche(this);
	}
	@Override
	public String toString() {
		return "OuvrierFiche [id : "+ id +" type "+ type+ "  date "+ date +" ouvrierDesignation=" + ouvrierDesignation + "]";
	}

}
