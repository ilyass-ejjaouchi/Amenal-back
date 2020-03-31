package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceptionDesignationRepository extends JpaRepository<ReceptionDesignation, Integer>{
	
	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and ds.receptionfiche.isValidated = false  ")
	List<ReceptionDesignation> findFournisseurAssoToFicheReception(@Param("fr")Fournisseur fr);
	
	
}
