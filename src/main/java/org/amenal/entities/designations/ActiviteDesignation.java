package org.amenal.entities.designations;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.amenal.entities.Lot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ActDesignation")
public class ActiviteDesignation extends Designation {

	@OneToOne
	private Lot lot;
	private String designation;
	@OneToMany(mappedBy = "activiteDesignation", cascade = CascadeType.ALL)
	private List<SousLotDesignation> sousLotDesignations;

	public String getDesignation() {

		if (fiche.getIsValidated())
			return designation;
		else
			return lot.getDesignation();
	}

}
