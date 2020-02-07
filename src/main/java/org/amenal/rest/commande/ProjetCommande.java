package org.amenal.rest.commande;

import java.util.ArrayList;
import java.util.List;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjetCommande {
	
	private String titre;
	
	private List<String> fichierTypes = new ArrayList<String>();
	

}
