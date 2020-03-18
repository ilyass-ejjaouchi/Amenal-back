package org.amenal.dao;


import java.util.List;

import org.amenal.entities.Fournisseur;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Materiel;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationAssoRepository extends JpaRepository<LocationAsso, Integer> {

	

	LocationAsso findByMaterielAndFourniseurAndProjet(Materiel mat, Fournisseur fr, Projet p);
	
	List<LocationAsso> findByFourniseurAndProjet( Fournisseur fr, Projet p);
	List<LocationAsso> findByFourniseur( Fournisseur fr);
	List<LocationAsso> findByMateriel(Materiel mat);
	
	@Query("SELECT distinct loc.materiel FROM LocationAsso loc WHERE loc.projet=:projet")
	List<Materiel> findMaterilByProjet(@Param("projet")Projet projet);
	



}
