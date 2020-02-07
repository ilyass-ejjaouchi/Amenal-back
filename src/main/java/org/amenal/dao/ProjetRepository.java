package org.amenal.dao;

import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository extends JpaRepository<Projet, Integer>{

	Projet findByTitre(String title);

}
