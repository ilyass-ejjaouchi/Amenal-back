package org.amenal.dao;

import org.amenal.entities.designations.StockDesignation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockDesignationRepository extends JpaRepository<StockDesignation, Integer> {
	
	StockDesignation findByDesignation(String designation);

}
