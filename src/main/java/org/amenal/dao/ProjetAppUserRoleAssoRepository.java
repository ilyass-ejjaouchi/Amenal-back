package org.amenal.dao;

import java.util.List;

import org.amenal.entities.security.ProjetAppUserRoleAsso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjetAppUserRoleAssoRepository  extends JpaRepository<ProjetAppUserRoleAsso, Integer>{
	
	
	
	
	
	ProjetAppUserRoleAsso findByUserUsernameAndProjetIdAndRoleRole(String username , Integer  id , String role);

}
