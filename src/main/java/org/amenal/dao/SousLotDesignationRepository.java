package org.amenal.dao;

import java.util.List;

import org.amenal.entities.designations.SousLotDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SousLotDesignationRepository extends JpaRepository<SousLotDesignation, Integer> {

	@Query("select sld from  SousLotDesignation sld "
			+ "where sld.activiteDesignation.fiche.isValidated = 'false' and sld.sousLot.id =:slotId")
	List<SousLotDesignation> findSousLotDesignationWhereFicheNonValid(@Param("slotId") Integer slotId);

	@Query("select sld from  SousLotDesignation sld "
			+ "where sld.activiteDesignation.fiche.isValidated = 'false' and sld.sousLot.id =:slotId and sld.activiteDesignation.fiche.projet.id =:pid")
	List<SousLotDesignation> findSousLotDesignationByProjetIdWhereFicheNonValid(@Param("slotId") Integer slotId,
			@Param("pid") Integer pid);
}
