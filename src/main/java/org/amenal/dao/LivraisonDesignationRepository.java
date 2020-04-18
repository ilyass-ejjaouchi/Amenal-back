package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Article;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivraisonDesignationRepository extends JpaRepository<LivraisonDesignation, Integer> {

	@Query("select ds from LivraisonDesignation ds WHERE ds.articleLvr.id=:articleId and ds.livraisonFiche.isValidated = false ")
	List<LivraisonDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId") Integer articleId);

	@Query("select ds from LivraisonDesignation ds WHERE ds.articleLvr.categorie.id=:catId and ds.livraisonFiche.isValidated = false ")
	List<LivraisonDesignation> findDesignationByCategorieArticleAndFicheNotValid(@Param("catId") Integer catId);

	@Query("select ds from LivraisonDesignation ds WHERE ds.articleLvr=:art and ds.livraisonFiche.isValidated = false and ds.livraisonFiche.projet =:p ")
	List<LivraisonDesignation> findDesignationByArticleAndProjet(@Param("art") Article art, @Param("p") Projet p);

}
