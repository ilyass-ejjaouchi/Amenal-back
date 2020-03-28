package org.amenal.entities.fiches;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.ReceptionDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("reception")
@Setter@Getter
@NoArgsConstructor
public class ReceptionFiche extends Fiche {

	private String type = FicheTypeEnum.RCP.getCode();

	@OneToMany(mappedBy = "receptionfiche")
	private List<ReceptionDesignation> receptionDesignations;

}
