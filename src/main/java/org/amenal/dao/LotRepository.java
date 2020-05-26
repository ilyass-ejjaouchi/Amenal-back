package org.amenal.dao;

import org.amenal.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Integer> {
	
	 Lot findByDesignation(String ds);

}
