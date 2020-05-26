package org.amenal.rest.commande;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class SousLotCommande {
	
	private Integer lotId;
	private String designation;
	private String unite;
	
	public Integer getLotId() {
		return lotId;
	}
	public void setLotId(Integer lotId) {
		this.lotId = lotId;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation.toUpperCase();
	}
	public String getUnite() {
		return unite;
	}
	public void setUnite(String unite) {
		this.unite = unite;
	}
	
	
	

}
