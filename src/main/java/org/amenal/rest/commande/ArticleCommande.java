package org.amenal.rest.commande;




import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ArticleCommande {
	private Integer id;
  	private String designation;
  	private Boolean stockable; 
  	private String unite;
  	
  	private Integer categorieID;
}
