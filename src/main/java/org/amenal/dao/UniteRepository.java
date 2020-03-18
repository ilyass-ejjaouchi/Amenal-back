package org.amenal.dao;

import org.amenal.entities.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniteRepository extends JpaRepository<Unite, Integer>{
	
	public Unite findByUnite(String unite);

}
