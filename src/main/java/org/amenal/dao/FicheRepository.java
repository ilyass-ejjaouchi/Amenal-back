package org.amenal.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.amenal.entities.fiches.Fiche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FicheRepository<T extends Fiche> extends JpaRepository<T, Integer> {

	//if date != it will return a single result
	@Query("select f from Fiche as f where  f.projet.id = :projetID  AND (:ficheType IS NULL OR type=:ficheType ) AND"
			+ " ( :dateCreation is NULL OR f.date=:dateCreation ) "
			+ "order by date(date) asc ")
	List<T> findByProjetAndTypeFicheAndDate(@Param("projetID")Integer projetID ,@Param("ficheType") String ficheType,
			@Param("dateCreation") LocalDate dateCreation);
	
	
	
}
