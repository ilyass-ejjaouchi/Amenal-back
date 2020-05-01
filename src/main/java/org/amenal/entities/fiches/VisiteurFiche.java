package org.amenal.entities.fiches;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.VisiteurDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("visiteur")
@Getter @Setter @NoArgsConstructor
public class VisiteurFiche extends Fiche {
	
private String type = FicheTypeEnum.VST.getCode();
	
	private String alpha = "f";

	@OneToMany(mappedBy="fiche",cascade=CascadeType.REMOVE )
	private List<VisiteurDesignation> visiteurDesignations;
}
