package org.amenal.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
@Entity
public class Ouvrier {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Temporal(TemporalType.DATE)
	private Date dateNaissance;
	@Temporal(TemporalType.DATE)
	private Date dateRecrutement;
	private String tele;
	private String appreciation;
	@Transient
	private int nbrJours;
	private QualificationOuvrierEnum qualification;
	
	
	private int nbFile = 0;
	private String cin;
	private String nom;
	private String prenom;
	@ManyToMany
	private List<Projet> projets;
	
	public void addProjet(Projet projet) {
		// TODO Auto-generated method stub
		if (projets == null) {
			projets = new ArrayList<Projet>();
		}
		this.projets.add(projet);
		
	}
	
	
	
	
	
	
	
}
