package org.amenal.dao;

import org.amenal.entities.Document;
import org.amenal.entities.designations.DocDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DocDesignationRepository extends JpaRepository<DocDesignation, Integer> {
	
	@Query("select d from DocDesignation d where d.document=:d and d.fiche.isValidated=false")
	DocDesignation findByDocument(@Param("d") Document d);

}
