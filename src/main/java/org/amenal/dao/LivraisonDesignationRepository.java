package org.amenal.dao;

import java.util.List;

import org.amenal.entities.designations.LivraisonDesignation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivraisonDesignationRepository 
extends JpaRepository<LivraisonDesignation, Integer> {

}
