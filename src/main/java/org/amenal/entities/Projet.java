package org.amenal.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.amenal.entities.security.AppUser;

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
	private String intitule;
	private String abreveation;
	private String description;
	private LocalDate debut;
	private LocalDate fin;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<FicheTypeEnum> fichierTypes = new ArrayList<FicheTypeEnum>();

	@OneToMany(mappedBy = "projet", cascade = { CascadeType.PERSIST , CascadeType.MERGE })
	private List<Fiche> fichiers = new ArrayList<Fiche>();

	@ManyToMany()
	@JoinTable(name = "projet_ouvrier", joinColumns = {
			@JoinColumn(name = "fk_projet", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "fk_ouvrier", nullable = true) })
	private List<Ouvrier> ouvriers = new ArrayList<Ouvrier>();

	@ManyToMany()
	@JoinTable(name = "projet_destination", joinColumns = {
			@JoinColumn(name = "fk_projet", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "fk_dst", nullable = true) })
	private List<Destination> destinations = new ArrayList<Destination>();
	
	@ManyToMany()
	@JoinTable(name = "projet_document", joinColumns = {
			@JoinColumn(name = "fk_projet", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "fk_document", nullable = true) })
	private List<Document> documents = new ArrayList<Document>();
	
	@ManyToMany(cascade = CascadeType.REFRESH)
	@JoinTable(name = "projet_visiteur", joinColumns = {
			@JoinColumn(name = "fk_projet", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "fk_visiteur", nullable = true) })
	private List<Visiteur> visiteurs = new ArrayList<Visiteur>();
	
	
	

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

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule.toUpperCase();;
	}

	public String getAbreveation() {
		return abreveation;
	}

	public void setAbreveation(String abreveation) {
		this.abreveation = abreveation.toUpperCase();;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.toUpperCase();;
	}
	
	


}
