package org.amenal.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OuvrierDesignationRepository extends JpaRepository<OuvrierDesignation, Integer> {

	@Query("select ds.ouvrier from OuvrierDesignation ds WHERE ds.OuvrierFiche.id=:ficheID ")
	List<Ouvrier> findOuvrierByFiche(@Param("ficheID") Integer ficheID);

	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier.id=:ouvID ")
	List<OuvrierDesignation> findDesignationByOuvrierID(@Param("ouvID") Integer ouvID);

	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier.id=:ouvID and OuvrierFiche.isValidated = false ")
	List<OuvrierDesignation> findDesignationByOuvrierIDAndFicheNotValid(@Param("ouvID") Integer ouvID);

	@Query("select ds.qualification as qual , sum(ds.travail) as qt from OuvrierDesignation ds WHERE"
			+ " ds.OuvrierFiche.projet.id=:projetID AND ds.OuvrierFiche.date =:date group by ds.qualification")
	List<Map<String, Object>> findDesignationByDateAndProjet(@Param("projetID") Integer projetID,
			@Param("date") LocalDate date);

}
