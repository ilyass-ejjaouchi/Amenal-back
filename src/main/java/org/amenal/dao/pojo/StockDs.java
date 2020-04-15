package org.amenal.dao.pojo;

import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockDs {

	private Double quantite;
	private Article article;
	private CategorieArticle categorie;

	public StockDs(Article article, Double quantite, CategorieArticle categorie) {
		super();
		this.article = article;
		this.quantite = quantite;
		this.categorie = categorie;
	}

}
