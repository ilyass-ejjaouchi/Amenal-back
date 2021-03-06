package org.amenal.entities.fiches;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.DocDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("document")
@Setter
@Getter
@NoArgsConstructor
public class DocFiche extends Fiche implements Serializable {
	private String type = FicheTypeEnum.DOC.getCode();
	
	private String alpha = "g";

	@OneToMany(mappedBy = "fiche" , cascade = CascadeType.ALL)
	private List<DocDesignation> docDesignations = new ArrayList<DocDesignation>();
}
