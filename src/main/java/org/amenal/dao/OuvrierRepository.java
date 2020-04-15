package org.amenal.dao;

import org.amenal.entities.Ouvrier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OuvrierRepository extends JpaRepository<Ouvrier, Integer> {

	Ouvrier findByCin(String cin);

	Ouvrier findByNomAndPrenom(String nom, String prenom);

}
