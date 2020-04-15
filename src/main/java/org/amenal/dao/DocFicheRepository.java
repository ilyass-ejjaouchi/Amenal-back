package org.amenal.dao;

import org.amenal.entities.Projet;
import org.amenal.entities.fiches.DocFiche;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocFicheRepository extends FicheRepository<DocFiche>{
	
	@Query("SELECT f from DocFiche f where f.isValidated=false and f.projet=:projet")
	DocFiche findLastDocFicheByProjet(@Param("projet") Projet p);

}
