package org.amenal.dao;

import org.amenal.entities.QualificationOuvrier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationOuvrierRepository extends JpaRepository<QualificationOuvrier, Integer> {

	public QualificationOuvrier findByCode(String code);
}
