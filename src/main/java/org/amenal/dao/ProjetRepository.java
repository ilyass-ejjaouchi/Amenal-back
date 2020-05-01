package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Document;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjetRepository extends JpaRepository<Projet, Integer>{

	Projet findByAbreveation(String ab);
	
	@Query("select p.ouvriers from Projet p where p.id = :projetID  ")
	List<Ouvrier> findOuvrierByProjetID(@Param("projetID")Integer projetID);
	
	@Query("select p from Projet p join p.ouvriers ouv where ouv.id = :ouvrierID  ")
	List<Projet> findProjetByOuvrierID(@Param("ouvrierID")Integer ouvrierID);
	
	@Query("select p from Projet p  where p.id IN (:ouvrierIDs)  ")
	List<Projet> findProjetByIDs(@Param("ouvrierID")List<Integer> ouvrierIDs);
	
	@Query("select p.documents from Projet p where p.id = :projetID  ")
	List<Document> findDocumentByProjetID(@Param("projetID")Integer projetID);

	
	
	

}
