package org.amenal.dao;

import java.util.List;
import java.util.Map;

import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.Projet;
import org.amenal.entities.ReceptionAsso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReceptionAssoRepository extends JpaRepository<ReceptionAsso, Integer> {

	ReceptionAsso findByFournisseurIdAndArticleId(Integer fr, Integer art);

	List<ReceptionAsso> findByFournisseurAndArticle(Fournisseur f, Article a);

	List<ReceptionAsso> findByFournisseur(Fournisseur fr);

	@Query("SELECT rec FROM ReceptionAsso rec where rec.fournisseur =:fr and rec.categorie is null and rec.article is null")
	ReceptionAsso findByFournisseurAndOthersNull(@Param("fr") Fournisseur fr);

	@Query("SELECT rec.article FROM ReceptionAsso rec where rec.categorie.categorie=:cat ")
	List<Article> findArticleByCat(@Param("cat") String cat);
	
	@Query("SELECT rec FROM ReceptionAsso rec join rec.projets ps  where ps =:p and rec.article is not null")
	List<ReceptionAsso> findFournisseurArticleAssoToProjet(@Param("p") Projet projet);

	@Query("SELECT f FROM Fournisseur f left join f.receptionAsso rec where rec = null")
	List<Fournisseur> findFournisseurNotAsso();

	@Query("SELECT rec FROM ReceptionAsso rec ORDER BY rec.fournisseur , rec.categorie")
	List<ReceptionAsso> findByOrderByFournisseurAndCategorie();

}
