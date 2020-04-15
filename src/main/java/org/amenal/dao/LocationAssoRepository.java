package org.amenal.dao;

import java.util.List;

import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LocationAssoRepository extends JpaRepository<LocationAsso, Integer> {

	LocationAsso findByMaterielAndFourniseurAndProjet(Article mat, Fournisseur fr, Projet p);

	List<LocationAsso> findByFourniseurAndProjet(Fournisseur fr, Projet p);

	List<LocationAsso> findByFourniseur(Fournisseur fr);

	List<LocationAsso> findByMateriel(Article mat);

	@Query("SELECT distinct loc.projet.intitule FROM LocationAsso loc WHERE loc.fourniseur=:f and loc.materiel=:mat")
	List<String> findProjetByFourniseurAndMateriel(@Param("f") Fournisseur fr, @Param("mat") Article mat);

	@Query("SELECT distinct loc.projet.intitule FROM LocationAsso loc WHERE loc.fourniseur=:f")
	List<String> findProjetByfournisseur(@Param("f") Fournisseur f);

	@Transactional
	@Modifying
	@Query("delete FROM LocationAsso WHERE fourniseur=:f and materiel=:mat")
	void deleteLocationAssoByFournisseurAndMateriel(@Param("f") Fournisseur fr, @Param("mat") Article mat);

	@Query("SELECT loc FROM LocationAsso loc WHERE loc.projet.id=:projetId and loc.materiel is not null")
	List<LocationAsso> findByProjet(@Param("projetId") Integer projet);

}
