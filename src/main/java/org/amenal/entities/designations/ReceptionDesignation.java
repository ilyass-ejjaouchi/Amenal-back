package org.amenal.entities.designations;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.fiches.ReceptionFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("RecDesignation")
@Getter
@Setter
@NoArgsConstructor
public class ReceptionDesignation extends Designation {

	private Integer id;
	private String libelle;
	private String unitee;
	private String categorie; 
	private Double quantite;
	private String fournisseurNom;
	private String observation;
	
	@OneToMany(mappedBy="receptionDesignationOuv" , cascade = CascadeType.REMOVE)
	List<OuvrierDesignation> ouvrierDesignations = new ArrayList<OuvrierDesignation>();
	
	@OneToMany(mappedBy="receptionDesignationLoc" , cascade = CascadeType.REMOVE)
	List<LocationDesignation> locationDesignations = new ArrayList<LocationDesignation>();

	@ManyToOne
	private Article article;

	@ManyToOne
	private Fournisseur recFournisseur;
	
	@ManyToOne
	private ReceptionFiche receptionfiche;

	@Override
	public String toString() {
		return "ReceptionDesignation [id=" + id + ", libelle=" + libelle + ", unite=" + unitee + "]";
	}
	
	

}
