package org.amenal.rest.representation;






import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticlePresentation {
	private Integer id;
  	private String designation;
  	private Boolean stockable; 
	private Boolean isAssoWithProjet;
	private String categorie;
  	private String unite;
  	
}
