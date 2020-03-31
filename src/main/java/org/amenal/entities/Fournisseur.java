package org.amenal.entities;

import java.util.List;

import javax.persistence.*;

import org.amenal.entities.designations.ReceptionDesignation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter @Setter @NoArgsConstructor

public class Fournisseur  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fournisseurNom;
    
    @Transient
    private Article article;
    
    @Transient
    private Article materiel;
    
    @OneToMany(mappedBy="fourniseur")
    List<LocationAsso> locationAssos;
    
    @OneToMany(mappedBy="fournisseur")
    List<ReceptionAsso> receptionAsso;
    
    @OneToMany(mappedBy="recFournisseur")
    List<ReceptionDesignation> receptionDesignations;
    
    
    @Transient
	private Boolean isAssoWithProjet = false;
    
    

	@Override
	public String toString() {
		return "Fournisseur [id=" + id + ", libelle=" + fournisseurNom + ", locationAssos=" + locationAssos + "]";
	}

}
