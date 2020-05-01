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
	/**********************/
	@Query("SELECT rec FROM ReceptionAsso rec WHERE rec.fournisseur =:fr and rec.article=:art and rec.article NOT IN "
			+ "( SELECT ds.article FROM ReceptionDesignation ds "
			+ "WHERE ds.recFournisseur=:fr and ds.article=:art and ds.fiche.isValidated=false and ds.fiche.projet=:projet)")
	ReceptionAsso findByFournisseurIdAndArticleId(@Param("fr") Fournisseur fr, @Param("art") Article art,
			@Param("projet") Projet p);

	@Query("SELECT rec FROM ReceptionAsso rec WHERE rec.fournisseur=:fr and rec.article NOT IN "
			+ "( SELECT ds.article FROM ReceptionDesignation ds WHERE ds.recFournisseur=:fr and ds.fiche.isValidated=false and ds.fiche.projet=:projet)")
	List<ReceptionAsso> findByFournisseurAndArticleNotAssoToFiche(@Param("fr") Fournisseur f,
			@Param("projet") Projet p);

	@Query("SELECT rec FROM ReceptionAsso rec WHERE rec.fournisseur=:fr and rec.article.categorie=:cat and rec.article NOT IN "
			+ "( SELECT ds.article FROM ReceptionDesignation ds "
			+ "WHERE ds.recFournisseur=:fr and ds.article.categorie=:cat and ds.fiche.isValidated=false and ds.fiche.projet=:projet)")
	List<ReceptionAsso> findByCategorieAndArticleNotAssoToFiche(@Param("fr") Fournisseur f,
			@Param("cat") CategorieArticle cat, @Param("projet") Projet p);

	/*****************************/
	ReceptionAsso findByFournisseurAndArticle(Fournisseur f, Article a);

	@Query("SELECT rec FROM ReceptionAsso rec where rec.fournisseur =:fr and rec.article.categorie =:cat")
	List<ReceptionAsso> findByFournisseurAndCategorie(@Param("fr") Fournisseur f, @Param("cat") CategorieArticle art);

	List<ReceptionAsso> findByFournisseur(Fournisseur fr);

	List<ReceptionAsso> findByArticle(Article ar);
	
	List<ReceptionAsso> findByArticleId(Integer ar);


	@Query("SELECT rec FROM ReceptionAsso rec where rec.fournisseur =:fr and rec.article.categorie.id =:idCat")
	List<ReceptionAsso> findByFournisseurAndCategorieId(Fournisseur fr, Integer idCat);

	@Query("SELECT rec FROM ReceptionAsso rec where rec.fournisseur =:fr and rec.article is null")
	ReceptionAsso findByFournisseurAndOthersNull(@Param("fr") Fournisseur fr);

	@Query("SELECT rec.article FROM ReceptionAsso rec where rec.article.categorie.categorie=:cat ")
	List<Article> findArticleByCat(@Param("cat") String cat);

	@Query("SELECT rec FROM ReceptionAsso rec join rec.projets ps  where ps =:p and rec.article is not null")
	List<ReceptionAsso> findFournisseurArticleAssoToProjet(@Param("p") Projet projet);

	@Query("SELECT f FROM Fournisseur f left join f.receptionAsso rec where rec = null")
	List<Fournisseur> findFournisseurNotAsso();

	@Query("SELECT rec FROM ReceptionAsso rec left join rec.article.categorie cat ORDER BY rec.fournisseur , cat")
	List<ReceptionAsso> findByOrderByFournisseurAndCategorie();

	@Query("SELECT distinct p.intitule FROM ReceptionAsso loc join loc.projets p WHERE loc.fournisseur=:f and loc.article=:art")
	List<String> findProjetByfournisseurAndArticle(@Param("f") Fournisseur f, @Param("art") Article art);

	@Query("SELECT distinct p.intitule FROM ReceptionAsso loc join loc.projets p WHERE loc.fournisseur=:f")
	List<String> findProjetByfournisseur(@Param("f") Fournisseur f);

}
