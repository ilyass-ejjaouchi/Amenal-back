package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.amenal.entities.fiches.LivraisonFiche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivraisonFicheRepository extends FicheRepository<LivraisonFiche> {
	
	@Query("select ds.articleLvr.id as artId , ds.quantite as qt from LivraisonFiche f join f.livraisonDesignations ds "
			+ "WHERE f.date=:date and f.projet.id=:projetId")
	List<Map<String,Object>> findLivraisonDesignationByDate(@Param("date") LocalDate date , @Param("projetId") Integer projetId);
	

}
