package org.amenal.dao;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.OuvrierDesignation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OuvrierDesignationRepository extends JpaRepository<OuvrierDesignation, Integer>{



}
