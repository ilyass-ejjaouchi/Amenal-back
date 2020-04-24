package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Visiteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VisiteurRepository extends JpaRepository<Visiteur, Integer>{
	
	@Query("select v from Visiteur v join v.projets p where p.id =:id")
	List<Visiteur> findByProjetId(@Param("id") Integer id);

}
