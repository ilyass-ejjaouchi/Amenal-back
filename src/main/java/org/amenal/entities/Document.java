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
import javax.persistence.Transient;

import org.amenal.entities.designations.DocDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String intitule;
	
	@Transient
	private Boolean isAsso;


	@ManyToMany(mappedBy = "documents", cascade = CascadeType.REFRESH)
	private List<Projet> projets;
	
	@OneToMany(mappedBy ="document" , cascade= CascadeType.MERGE)
	private List<DocDesignation> docDesignations = new ArrayList<DocDesignation>();
	
	

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule.toUpperCase();
	}

}
