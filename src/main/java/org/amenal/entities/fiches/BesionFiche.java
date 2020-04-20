package org.amenal.entities.fiches;

import java.io.Serializable;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.BesionDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("besion")
@Setter
@Getter
@NoArgsConstructor
public class BesionFiche extends Fiche implements Serializable {

	private String type = FicheTypeEnum.BSN.getCode();
	private String alpha = "e";


	@OneToMany(mappedBy = "besionFiche")
	private List<BesionDesignation> besionDesignations;

}
