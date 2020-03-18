package org.amenal.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	private LocalDate dateNaissance;
	private LocalDate dateRecrutement;
	private String tele;
	private String appreciation;
	@Transient
	private int nbrJours;
	@ManyToOne
	@JoinColumn(name="FK_QUAL")
	private QualificationOuvrier qualification;
	
	
	private int nbFile = 0;
	private String cin;
	private String nom;
	private String prenom;
	@ManyToMany(mappedBy="ouvriers" , cascade=CascadeType.REFRESH )
	private List<Projet> projets;
	
	
	public void addProjet(Projet projet) {
		// TODO Auto-generated method stub
		if (projets == null) {
			projets = new ArrayList<Projet>();
		}
		this.projets.add(projet);
		
	}
	
	
	
	
	
	
	
}
