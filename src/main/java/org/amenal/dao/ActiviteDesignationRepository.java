package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.amenal.entities.designations.ActiviteDesignation;
import org.amenal.entities.designations.EntreeDesignation;
import org.amenal.entities.designations.SousLotDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActiviteDesignationRepository extends JpaRepository<ActiviteDesignation, Integer> {

	@Query("select entree from SousLotDesignation slotDs join slotDs.entreeDesignations as entree where "
			+ " slotDs.activiteDesignation.fiche.date=:date "
			+ "and slotDs.activiteDesignation.fiche.projet.id=:projetId  ")
	List<EntreeDesignation> getentreeficheAct(@Param("date") LocalDate date, @Param("projetId") Integer projetId);
	
	
	@Query("select ds from ActiviteDesignation ds "
			+ "where ds.fiche.isValidated = 'false' and ds.lot.id =:lotId")
	List<ActiviteDesignation> findLotDesignationWhereFicheNonValid(@Param("lotId") Integer lotId);
	
	@Query("select ds from ActiviteDesignation ds "
			+ "where ds.fiche.isValidated = 'false' and ds.lot.id =:lotId and ds.fiche.projet.id =:pid")
	List<ActiviteDesignation> findSousLotDesignationByProjetIdWhereFicheNonValid(@Param("lotId") Integer lotId,
			@Param("pid") Integer pid);

}
