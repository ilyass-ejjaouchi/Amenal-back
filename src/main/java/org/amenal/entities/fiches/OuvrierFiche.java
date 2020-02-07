package org.amenal.entities.fiches;

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

public class OuvrierFiche extends Fiche {
	
	@OneToMany(mappedBy = "ouvrieFichier")
	private List<OuvrierDesignation> ouvrierDesignations = new ArrayList<OuvrierDesignation>();
	
	
	
	public void addLignedesignation(OuvrierDesignation ouvDesignation) {
		// TODO Auto-generated method stub
		if (ouvrierDesignations == null) {
			ouvrierDesignations = new ArrayList<OuvrierDesignation>();
		}
		ouvrierDesignations.add(ouvDesignation);
		ouvDesignation.setOuvrieFichier(this);

	}


}
