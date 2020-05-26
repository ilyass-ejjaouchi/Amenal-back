package org.amenal.entities.designations;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.amenal.entities.SousLot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SousLotDesignation {

	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String designation;

	private String unite;
	@OneToOne
	private SousLot sousLot;

	private Double avancement;

	@ManyToOne
	private ActiviteDesignation activiteDesignation;

	@OneToMany(mappedBy = "sousLotDesignation", cascade = CascadeType.ALL)
	private List<EntreeDesignation> entreeDesignations = new ArrayList<EntreeDesignation>();
	
	public String getDesignation() {
		if(this.activiteDesignation.fiche.getIsValidated())
			
		return designation;
		else
			return sousLot.getDesignation();
	}

	public String getUnite() {
		if(this.activiteDesignation.fiche.getIsValidated())
			
			return unite;
			else
				return sousLot.getUnite().getUnite();
	}
	
	

}
