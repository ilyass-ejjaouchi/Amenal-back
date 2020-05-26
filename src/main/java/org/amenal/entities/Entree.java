package org.amenal.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Entree {
	
	public static final String OUVRIER ="ouv";
	public static final String ARTICLE ="art";

	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String type;
	
	@ManyToOne
	private Article article;
	
	@ManyToOne
	private QualificationOuvrier qualification;
	
	private Double quantiteEstimer;
	
	@ManyToOne
	@JoinColumn(name="FK_SOUS_LOT_ID")
	private SousLot sousLot;
	
}
