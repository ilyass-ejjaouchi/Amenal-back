package org.amenal.entities.fiches;

import java.io.Serializable;
import java.util.ArrayList ;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.LocationDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("location")
@Setter@Getter
@NoArgsConstructor
public class LocationFiche extends Fiche implements Serializable{
	
	private String type = FicheTypeEnum.LOC.getCode();


	
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="locationFiche")
	private List<LocationDesignation> locationDesignations ;
	

	
}
