package org.amenal.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CategorieArticle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String categorie;
	
	@OneToMany( mappedBy="categorie"  , cascade= {CascadeType.REMOVE , CascadeType.MERGE})
	private List<Article> articles = new  ArrayList<Article>() ;
	
	private Boolean showCat;
	
	@Transient
	private Boolean isAssoWithProjet = false;
	
	

	@Override
	public String toString() {
		return "CategorieArticle [categorie=" + categorie + ", articles=" + articles + "]";
	}



	public String getCategorie() {
		return categorie;
	}


	public void setCategorie(String categorie) {
		this.categorie = categorie.trim().toUpperCase();
	}
	
	

	
}
