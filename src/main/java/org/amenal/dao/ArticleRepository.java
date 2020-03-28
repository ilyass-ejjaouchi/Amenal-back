package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
	
	
	@Query("select ar from Article ar where ar.categorie is null")
	public List<Article> findAllMateriels();
	
	@Query("select ar from Article ar where ar.categorie is not null")
	public List<Article> findAllArticles();
	
	 

}
