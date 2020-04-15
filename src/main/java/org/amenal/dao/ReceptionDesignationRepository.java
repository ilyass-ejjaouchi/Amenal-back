package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;

import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceptionDesignationRepository extends JpaRepository<ReceptionDesignation, Integer> {

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and ds.receptionfiche.isValidated = false  ")
	List<ReceptionDesignation> findFournisseurAssoToFicheReception(@Param("fr") Fournisseur fr);

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and ds.receptionfiche.isValidated = false and ds.receptionfiche.projet=:projet  ")
	List<ReceptionDesignation> findByFournisseurAndProjetAssoToFicheReception(@Param("fr") Fournisseur fr,
			@Param("projet") Projet projet);

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and ds.receptionfiche.isValidated = false and ds.article=:mat and ds.receptionfiche.projet=:projet  ")
	List<ReceptionDesignation> findByFournisseurAndProjetAndMaterielAssoToFicheReception(@Param("fr") Fournisseur fr,
			@Param("projet") Projet projet, @Param("mat") Article materiel);

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and ds.article =:art and ds.receptionfiche.isValidated = false  ")
	List<ReceptionDesignation> findFournisseurArticleAssoToFicheReception(@Param("fr") Fournisseur fr,
			@Param("art") Article art);

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur=:fr "
			+ "and  ds.article.categorie=:cat and ds.receptionfiche.isValidated = false  ")
	List<ReceptionDesignation> findFournisseurArticleCategorieAssoToFicheReception(@Param("fr") Fournisseur fr,
			@Param("cat") CategorieArticle cat);

	@Query("select new org.amenal.dao.pojo.StockDs( rec.article , SUM(rec.quantite) , rec.article.categorie   )"
			+ "from ReceptionDesignation rec WHERE"
			+ " rec.receptionfiche.projet.id=:projetID AND rec.receptionfiche.date =:date "
			+ "group by rec.article , rec.article.categorie  order by rec.article.categorie.categorie asc")
	List<StockDs> findDesignationByDateAndProjet(@Param("projetID") Integer projetID, @Param("date") LocalDate date);

	@Query("select ds from ReceptionDesignation ds WHERE ds.article.id=:articleId and ds.receptionfiche.isValidated = false ")
	List<ReceptionDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId") Integer articleId);

	@Query("select ds from ReceptionDesignation ds WHERE ds.article.categorie.id=:catId and ds.receptionfiche.isValidated = false ")
	List<ReceptionDesignation> findDesignationByCategorieAndFicheNotValid(@Param("catId") Integer catId);

	@Query("select ds from ReceptionDesignation ds WHERE ds.recFournisseur.id=:fourID and ds.receptionfiche.isValidated = false ")
	List<ReceptionDesignation> findDesignationByfournisseurIDAndFicheNotValid(@Param("fourID") Integer fourID);
	
	
	@Query("select ds from ReceptionDesignation ds WHERE ds.categorie=:cat and ds.receptionfiche.isValidated = false and ds.receptionfiche.projet=:p")
	ReceptionDesignation FindByCategorieAndFicheNotValid(@Param("cat") String cat , @Param("p") Projet p);

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
