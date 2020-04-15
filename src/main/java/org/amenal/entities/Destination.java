package org.amenal.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.amenal.entities.designations.LivraisonDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Destination {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String destination;
	@ManyToMany(mappedBy = "destinations", cascade = CascadeType.REFRESH)
	private List<Projet> projets = new ArrayList<Projet>();
	
	@OneToMany(mappedBy="destination")
	private List<LivraisonDesignation> livraisonDesignations;
	
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination.trim().toUpperCase();
	}

	
}
