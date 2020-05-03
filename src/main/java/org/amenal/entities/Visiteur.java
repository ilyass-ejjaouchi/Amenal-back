package org.amenal.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Visiteur {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String nom;
	String organisme;
	@ManyToMany(mappedBy = "visiteurs", cascade = CascadeType.REFRESH)
	private List<Projet> projets;

	public void setNom(String nom) {
		this.nom = nom.toUpperCase();
	}

	public void setOrganisme(String organisme) {
		this.organisme = organisme.toUpperCase();
	}

}
