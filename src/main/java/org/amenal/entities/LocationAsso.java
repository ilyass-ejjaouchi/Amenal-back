package org.amenal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.CriteriaBuilder.In;

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
	Materiel materiel;

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

