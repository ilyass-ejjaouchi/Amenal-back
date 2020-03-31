package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.LocationDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationDesignationRepository extends JpaRepository<LocationDesignation, Integer>{
	
	@Query("select ds from LocationDesignation ds WHERE ds.materiel.id=:articleId and ds.locationFiche.isValidated = false ")
	List<LocationDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId")Integer articleId);
	
	@Query("select ds from LocationDesignation ds WHERE ds.fournisseur=:fr "
			+ "and ds.locationFiche.isValidated = false  ")
	List<LocationDesignation> findFournisseurAssoToFiche(@Param("fr")Fournisseur fr);
	
	
	
	

}
