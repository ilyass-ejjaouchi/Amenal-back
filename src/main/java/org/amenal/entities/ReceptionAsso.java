package org.amenal.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReceptionAsso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@ManyToOne
	Article article;

	@ManyToOne
	Fournisseur fournisseur;

	@ManyToMany
	List<Projet> projets;

	Boolean isAssoWithProjet;

	/*@ManyToOne
	CategorieArticle categorie;*/

	@Override
	public String toString() {
		return "LocationAsso [materiel=" + article.getId() + "]";
	}

}
