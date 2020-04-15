package org.amenal.entities.designations;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.fiches.LocationFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("LDesignation")
@Getter
@Setter
@NoArgsConstructor
public class LocationDesignation extends Designation {
	private static final long serialVersionUID = 1L;

	private String libelle;

	private String unite;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double travailleLoc;
	private Integer quantite;
	private String fournisseurNom;
	private String observation;
	
	@ManyToOne
	private Article materiel;
	
	@ManyToOne
	private Fournisseur fournisseur;

	@ManyToOne
	private LocationFiche locationFiche;
	
	@ManyToOne()
	ReceptionDesignation receptionDesignationLoc;

	@Override
	public String toString() {
		return "LocationDesignation [libelle=" + libelle + ", unite=" + unite + ", tempsDebut=" + tempsDebut
				+ ", tempsFin=" + tempsFin + ", quantite=" + quantite + ", fournisseurNom=" + fournisseurNom
				+ ", observation=" + observation + ", article=" + materiel.getId() + ", fournisseur=" + fournisseur.getId()
				+ ", locationFiche=" + locationFiche.getId() + "]";
	}
	
	

}
