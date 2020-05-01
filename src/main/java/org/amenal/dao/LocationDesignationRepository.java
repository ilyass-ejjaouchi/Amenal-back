package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationDesignationRepository extends JpaRepository<LocationDesignation, Integer>{
	
	@Query("select ds from LocationDesignation ds WHERE ds.materiel.id=:articleId and ds.fiche.isValidated = false ")
	List<LocationDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId")Integer articleId);
	
	@Query("select ds from LocationDesignation ds WHERE ds.fournisseur=:fr "
			+ "and ds.fiche.isValidated = false  ")
	List<LocationDesignation> findDesignationByfournisseurIDAndFicheNotValid(@Param("fr")Fournisseur fr);
	
	
	@Query("select  mat  as mat,sum(loc.travailleLoc) as somme from LocationDesignation loc join loc.materiel mat WHERE"
			+ " loc.fiche.projet.id=:projetID AND loc.travailleLoc IS NOT NULL  AND loc.fiche.date =:date group by mat")
	List<Map<String, Object>> findDesignationByDateAndProjet(@Param("projetID") Integer projetID,
			@Param("date") LocalDate date);
	
	@Query("select ds from LocationDesignation ds WHERE ds.fournisseur=:fr "
			+ "and ds.fiche.isValidated = false and ds.fiche.projet=:projet  ")
	List<LocationDesignation> findByFournisseurAndProjetAssoToFicheReception(@Param("fr") Fournisseur fr,
			@Param("projet") Projet projet);

	@Query("select ds from LocationDesignation ds WHERE ds.fournisseur=:fr "
			+ "and ds.fiche.isValidated = false and ds.materiel=:mat and ds.fiche.projet=:projet  ")
	List<LocationDesignation> findByFournisseurAndProjetAndMaterielAssoToFicheReception(@Param("fr") Fournisseur fr,
			@Param("projet") Projet projet, @Param("mat") Article materiel);
	
	
	
	
	

}
