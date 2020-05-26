package org.amenal.rest.commande;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EntreeCommande {

	String type;
	Integer entreeId;
	Double quantiteEstimer;
	Integer SousLotId;
	
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Double getQuantiteEstimer() {
		return quantiteEstimer;
	}
	public void setQuantiteEstimer(Double quantiteEstimer) {
		this.quantiteEstimer = quantiteEstimer;
	}
	public Integer getSousLotId() {
		return SousLotId;
	}
	public void setSousLotId(Integer sousLotId) {
		SousLotId = sousLotId;
	}
	public Integer getEntreeId() {
		return entreeId;
	}
	public void setEntreeId(Integer entreeId) {
		this.entreeId = entreeId;
	}
	
	

}
