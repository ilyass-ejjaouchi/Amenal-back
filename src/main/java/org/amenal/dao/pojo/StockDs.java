package org.amenal.dao.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StockDs {

	private String type;
	private String unite;
	private Double quantite;
	private String categorie;




	public StockDs(String type, String unite, Long quantite) {
		super();
		this.type = type;
		this.unite = unite;
		this.quantite = quantite.doubleValue();
	}

	public StockDs(String type, String unite, Double quantite, String categorie) {
		super();
		this.type = type;
		this.unite = unite;
		this.quantite = quantite;
		this.categorie = categorie;
	}

	
}
