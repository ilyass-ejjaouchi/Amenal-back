package org.amenal.entities.fiches;

import java.io.Serializable;
import java.time.LocalDate;


import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import javax.persistence.Transient;

import org.amenal.entities.Projet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_fichier")
@Getter @Setter @NoArgsConstructor
public abstract class Fiche implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer id;

	@Transient
	protected Integer count;
	

	
	
	
	protected LocalDate date;
	
	protected Boolean isValidated = false;
	
	@ManyToOne
	protected Projet projet;
	
	
	


}