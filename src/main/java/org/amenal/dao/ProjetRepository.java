package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjetRepository extends JpaRepository<Projet, Integer>{

	Projet findByTitre(String title);
	
	@Query("select p.ouvriers from Projet p where p.id = :projetID ")
	List<Ouvrier> findByProjetID(@Param("projetID")Integer projetID);
	

}
