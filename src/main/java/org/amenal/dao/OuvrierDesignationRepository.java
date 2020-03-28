package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.OuvrierDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OuvrierDesignationRepository extends JpaRepository<OuvrierDesignation, Integer>{

	@Query("select ds.ouvrier from OuvrierDesignation ds WHERE ds.OuvrierFiche.id=:ficheID ")
	List<Ouvrier> findOuvrierByFiche(@Param("ficheID")Integer ficheID);
	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier.id=:ouvID ")
	List<OuvrierDesignation> findDesignationByOuvrierID(@Param("ouvID")Integer ouvID);
	
	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier.id=:ouvID and OuvrierFiche.isValidated = false ")
	List<OuvrierDesignation> findDesignationByOuvrierIDAndFicheNotValid(@Param("ouvID")Integer ouvID);
	
	

}
