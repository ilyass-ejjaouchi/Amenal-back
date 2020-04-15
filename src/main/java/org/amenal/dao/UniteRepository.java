package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniteRepository extends JpaRepository<Unite, Integer>{
	
	public Unite findByUnite(String unite);
	
	public List<Unite> findByShowUnite(Boolean show);

}
