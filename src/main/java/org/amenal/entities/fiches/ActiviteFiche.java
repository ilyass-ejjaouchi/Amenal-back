package org.amenal.entities.fiches;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.ActiviteDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("activite")
@Setter
@Getter
@NoArgsConstructor
public class ActiviteFiche extends Fiche implements Serializable {

	private String type = FicheTypeEnum.ACT.getCode();
	private String alpha = "z";

	@OneToMany(mappedBy = "fiche" , cascade = CascadeType.ALL)
	private List<ActiviteDesignation> activiteDesignations;

}
