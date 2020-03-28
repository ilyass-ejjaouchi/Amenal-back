package org.amenal.dao;


import java.util.List;

import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationAssoRepository extends JpaRepository<LocationAsso, Integer> {

	

	LocationAsso findByArticleAndFourniseurAndProjet(Article mat, Fournisseur fr, Projet p);
	
	List<LocationAsso> findByFourniseurAndProjet( Fournisseur fr, Projet p);
	List<LocationAsso> findByFourniseur( Fournisseur fr);
	List<LocationAsso> findByArticle(Article mat);
	
	@Query("SELECT loc FROM LocationAsso loc WHERE loc.projet.id=:projetId and loc.article is not null")
	List<LocationAsso> findByProjet(@Param("projetId")Integer projet);
	



}
