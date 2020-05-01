package org.amenal.entities.designations;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.amenal.entities.Document;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.entities.fiches.Fiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DocDesignation")
@Getter
@Setter
@NoArgsConstructor
public class DocDesignation extends Designation {

	private String intitule;
	private Boolean disponibilite;

	@ManyToOne
	private Document document;

}
