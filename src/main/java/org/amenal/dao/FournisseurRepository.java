package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {

	@Query("select distinct f from Fournisseur f left join fetch f.locationAssos loc where loc.projet.id=:idProjet or loc.projet.id is null or loc is null ")
	List<Fournisseur> findByProjetId(@Param("idProjet") Integer idProjet);
	
	@Query("select distinct f from Fournisseur f join fetch f.locationAssos loc where loc.projet.id=:idProjet and loc.materiel.id=:idMateriel ")
	List<Fournisseur> findByProjetIdAndMaterielId(@Param("idProjet") Integer idProjet , @Param("idMateriel") Integer idMateriel);
	
	Fournisseur findByFournisseurNom(String nom);
	
}
