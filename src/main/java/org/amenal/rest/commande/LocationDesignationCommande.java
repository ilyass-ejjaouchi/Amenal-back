package org.amenal.rest.commande;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.ManyToOne;

import org.amenal.entities.fiches.LocationFiche;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class LocationDesignationCommande {
	

	private String libelle;

	private String unite;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double quantite;
	private String fournisseurNom;
	private String observation;

	private Integer idFiche;
	private Integer idProjet ;

}
