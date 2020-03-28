package org.amenal.rest.representation;

import java.util.List;

import org.amenal.entities.Article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor

public class FournisseurArticlePresentation {
	
	private Integer id;
	private String fournisseurNom;
	private Boolean isAssoWithProjet;
	private ArticlePresentation article; 
	private List<CategorieArticlePresentation> categories;
	
	

}
