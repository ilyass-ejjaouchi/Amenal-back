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
	

	private Integer idMateriel;
	private Integer fournisseurId;
	private LocalTime tempsDebut;
	private LocalTime tempsFin;
	private Double travailleLoc;
	private String quantite;
	private String fournisseurNom;
	private String observation;
	private Boolean valid;
	private Integer idFiche;
	private Integer idProjet;

}
