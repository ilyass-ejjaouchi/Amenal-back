package org.amenal.dao;

import java.util.List;

import org.amenal.entities.designations.LocationDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationDesignationRepository extends JpaRepository<LocationDesignation, Integer>{
	
	@Query("select ds from LocationDesignation ds WHERE ds.article.id=:articleId and ds.locationFiche.isValidated = false ")
	List<LocationDesignation> findDesignationByArticleIDAndFicheNotValid(@Param("articleId")Integer articleId);

}
