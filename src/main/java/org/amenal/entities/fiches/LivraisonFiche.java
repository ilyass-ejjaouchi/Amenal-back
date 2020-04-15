package org.amenal.entities.fiches;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.LivraisonDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("livraison")
@NoArgsConstructor
public class LivraisonFiche extends Fiche{

	private String type = FicheTypeEnum.LVR.getCode();

	@OneToMany(mappedBy = "livraisonFiche" , cascade=CascadeType.REMOVE)
	private List<LivraisonDesignation> livraisonDesignations;

}
