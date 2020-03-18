package org.amenal.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.FicheTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Projet implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(unique = true)
	private String titre;

	@ElementCollection(fetch=FetchType.EAGER)
	private List<FicheTypeEnum> fichierTypes = new ArrayList<FicheTypeEnum>();

	@OneToMany(mappedBy = "projet", cascade = { CascadeType.ALL })
	private List<Fiche> fichiers = new ArrayList<Fiche>();
	
	
	@ManyToMany()
	 @JoinTable(name="projet_ouvrier", joinColumns= { @JoinColumn(name="fk_projet" , nullable=true) }, inverseJoinColumns = { @JoinColumn(name="fk_ouvrier" , nullable=true) } )
	private List<Ouvrier> ouvriers = new ArrayList<Ouvrier>();
	
	public void addOuvrier(Ouvrier ouvrier) {
		// TODO Auto-generated method stub
		if (ouvriers == null) {
			ouvriers = new ArrayList<Ouvrier>();
		}
		ouvriers.add(ouvrier);
		ouvrier.addProjet(this);
	}
	
	public void addFiche(Fiche fiche) {
		// TODO Auto-generated method stub
		if (fichiers == null) {
			fichiers = new ArrayList<Fiche>();
		}
		fichiers.add(fiche);
		fiche.setProjet(this);

	}

}
