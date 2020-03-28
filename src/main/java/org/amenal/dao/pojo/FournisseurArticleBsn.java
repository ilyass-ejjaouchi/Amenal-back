package org.amenal.dao.pojo;

import java.util.ArrayList;
import java.util.List;

import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.Fournisseur;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FournisseurArticleBsn {
	
	private Integer id;
	private String fournisseurNom;
	private Boolean isAssoWithProjet;
	private List<CategorieArticle> categories = new ArrayList<CategorieArticle>();
	@Override
	public String toString() {
		return "FournisseurArticleBsn [libelle=" + fournisseurNom + ", categories=" + categories + "]";
	}
	
	

}
