package org.amenal.rest.representation;



import javax.persistence.Entity;



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
  	private String unite;
  	
}
