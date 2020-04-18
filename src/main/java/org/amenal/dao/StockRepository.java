package org.amenal.dao;

import java.time.LocalDate;
import java.util.List;

import org.amenal.entities.designations.Stock;
import org.amenal.entities.designations.StockDesignation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Integer> {
	
	

	Stock findByCategorie(String categorie);

	@Query("select stck from Stock stck JOIN FETCH stck.stockDesignations where stck.stockFiche.projet.id=:projetId AND stck.stockFiche.date=:date ")
	List<Stock> findByprojetIdAndDate(@Param("projetId") Integer idProjet, @Param("date") LocalDate date);
	
	@Query("select ds from Stock stck JOIN stck.stockDesignations ds "
			+ "where stck.stockFiche.projet.id=:projetId AND stck.stockFiche.date=:date "
			+ "and stck.categorie NOT IN :list ")
	List<StockDesignation> findRecByprojetIdAndDate(@Param("projetId") Integer idProjet, @Param("date") LocalDate date , @Param("list") List<String> list );

}
