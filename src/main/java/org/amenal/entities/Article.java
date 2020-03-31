package org.amenal.entities;


import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor

public class Article {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	  	private String designation;
	  	private Boolean stockable; 
	  	@ManyToOne
		@JoinColumn(name="UNITE_ID")
	  	private Unite unite;
	  	@ManyToOne
		@JoinColumn(name="FK_CAT_ID")
	  	private CategorieArticle categorie;
	  	
	  	@Transient
		private Boolean isAssoWithProjet;
	  	
	  	@OneToMany(mappedBy="materiel")
	  	List<LocationAsso> locationAsso;

	  	
	  	
		@Override
		public String toString() {
			return "Article [designation=" + designation + "]";
		}
		
		
	  	
	  	
}
