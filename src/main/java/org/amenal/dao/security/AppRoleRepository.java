package org.amenal.dao.security;

import org.amenal.entities.security.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppRoleRepository extends JpaRepository<AppRole,Integer>{
public AppRole findByRole(String role);
}