package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.LocationDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationDesignationRepository extends JpaRepository<LocationDesignation, Integer>{
	
	@Query("select ds from LocationDesignation ds WHERE ds.materiel.id=:articleId and ds.locationFiche.isValidated = false ")
	List<LocationDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId")Integer articleId);
	
	@Query("select ds from LocationDesignation ds WHERE ds.fournisseur=:fr "
			+ "and ds.locationFiche.isValidated = false  ")
	List<LocationDesignation> findDesignationByfournisseurIDAndFicheNotValid(@Param("fr")Fournisseur fr);
	
	@Query("select new org.amenal.dao.pojo.StockDs(loc.libelle  , loc.unite , sum(loc.travailleLoc)  )"
			+ "from LocationDesignation loc WHERE"
			+ " loc.locationFiche.projet.id=:projetID AND loc.locationFiche.date =:date group by loc.libelle")
	List<StockDs> findDesignationByDateAndProjet(@Param("projetID") Integer projetID,
			@Param("date") LocalDate date);
	
	
	
	

}
