package org.amenal.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	  	private Unite unite;
	  	@ManyToOne
		@JoinColumn(name="FK_CAT_ID")
	  	private CategorieArticle categorie;
	  	
	  	@Transient
		private Boolean isAssoWithProjet;

	  	
	  	
		@Override
		public String toString() {
			return "Article [designation=" + designation + "]";
		}
		
		
	  	
	  	
}
