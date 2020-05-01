package org.amenal.dao;

import org.amenal.entities.designations.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Integer> {

}
