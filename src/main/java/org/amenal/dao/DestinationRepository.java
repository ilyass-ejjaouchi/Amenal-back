package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Destination;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DestinationRepository extends JpaRepository<Destination, Integer> {
	
	/*@Query("SELECT dst from Destination dst join dst.livraisonDesignations ds where"
			+ " dst.id=:id and ds.locationFiche.projet=:p and s.locationFiche.isValidated = false")
	public List<Destination> findByIdAndProjetAndFicheNonValid(@Param("dst") Integer dst, @Param("projet") Projet p);*/
	
	

}
