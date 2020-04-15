package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
	
	 Document findByIntitule(String intitule);

}
