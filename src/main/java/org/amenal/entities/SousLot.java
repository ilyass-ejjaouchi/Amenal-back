package org.amenal.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SousLot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String designation;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "UNITE_ID")
	private Unite unite;
	
	@Transient
	Boolean isAsso;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sousLot", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Entree> entrees = new ArrayList<Entree>();

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "FK_LOT_ID")
	private Lot lot;

	@JsonIgnore
	@ManyToMany(mappedBy = "sousLots" , cascade= CascadeType.REFRESH)
	List<Projet> projets = new ArrayList<Projet>();

}
