package org.amenal.dao;

import java.util.List;

import org.amenal.entities.CategorieArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategorieArticleRepository extends JpaRepository<CategorieArticle, Integer> {
	
	@Query("select cat from CategorieArticle cat where cat.showCat =:show ")
	List<CategorieArticle> findByShow(@Param("show") Boolean show);
	
	CategorieArticle findByCategorie(String cat);

}
