package org.amenal.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class LocationAsso {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@ManyToOne
	Article materiel;

	@ManyToOne
	Fournisseur fourniseur;

	@ManyToOne
	Projet projet;

	Boolean isAssoWithProjet;
	
	@Override
	public String toString() {
		return "LocationAsso [materiel=" + materiel.getId() + "]";
	}
	
	

}

