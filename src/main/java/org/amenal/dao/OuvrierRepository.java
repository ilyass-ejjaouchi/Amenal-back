package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OuvrierRepository extends JpaRepository<Ouvrier, Integer>{
	
	
	


}
