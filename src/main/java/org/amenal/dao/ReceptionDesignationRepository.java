package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;

import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceptionDesignationRepository extends JpaRepository<ReceptionDesignation, Integer> {

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and ds.receptionfiche.isValidated = false  ")
	List<ReceptionDesignation> findFournisseurAssoToFicheReception(@Param("fr") Fournisseur fr);

	@Query("select new org.amenal.dao.pojo.StockDs(rec.libelle , rec.unitee , SUM(rec.quantite) , rec.categorie   )"
			+ "from ReceptionDesignation rec WHERE"
			+ " rec.receptionfiche.projet.id=:projetID AND rec.receptionfiche.date =:date group by rec.libelle order by rec.categorie asc")
	List<StockDs> findDesignationByDateAndProjet(@Param("projetID") Integer projetID,
			@Param("date") LocalDate date);
	
	@Query("select ds from ReceptionDesignation ds WHERE ds.article.id=:articleId and ds.receptionfiche.isValidated = false ")
	List<ReceptionDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId")Integer articleId);
	
	@Query("select ds from ReceptionDesignation ds WHERE ds.article.categorie.id=:catId and ds.receptionfiche.isValidated = false ")
	List<ReceptionDesignation> findDesignationByCategorieAndFicheNotValid(@Param("catId")Integer catId);
	
	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur.id=:fourID and ds.receptionfiche.isValidated = false ")
	List<ReceptionDesignation> findDesignationByfournisseurIDAndFicheNotValid(@Param("fourID")Integer fourID);


}
