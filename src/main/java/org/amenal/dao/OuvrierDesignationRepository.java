package org.amenal.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.OuvrierDesignation;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OuvrierDesignationRepository extends JpaRepository<OuvrierDesignation, Integer> {

	@Query("select ds.ouvrier from OuvrierDesignation ds WHERE ds.fiche.id=:ficheID ")
	List<Ouvrier> findOuvrierByFiche(@Param("ficheID") Integer ficheID);

	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier.id=:ouvID ")
	List<OuvrierDesignation> findDesignationByOuvrierID(@Param("ouvID") Integer ouvID);

	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier.id=:ouvID and fiche.isValidated = false ")
	List<OuvrierDesignation> findDesignationByOuvrierIDAndFicheNotValid(@Param("ouvID") Integer ouvID);
	
	@Query("select ds from OuvrierDesignation ds WHERE ds.ouvrier=:ouv and ds.fiche.isValidated = false and ds.fiche.projet=:p")
	List<OuvrierDesignation> findDesignationByOuvrierIDAndProjetAndFicheNotValid(@Param("ouv") Ouvrier ouv,
			@Param("p") Projet p);

	@Query("select quali  as qual , sum(ds.travail) as qt  from OuvrierDesignation ds join ds.ouvrier.qualification quali WHERE"
			+ " ds.fiche.projet.id=:projetID AND ds.travail IS NOT NULL AND ds.fiche.date =:date group by  quali ")
	List<Map<String, Object>> findDesignationByDateAndProjet(@Param("projetID") Integer projetID,
			@Param("date") LocalDate date);

}
