package org.amenal.dao.security;

import java.util.List;

import org.amenal.entities.security.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
	public AppUser findByUsername(String username);

	@Query("select user from AppUser user where user.roles IS NOT EMPTY AND user.isRoot=false")
	List<AppUser> listerUserWithRole();

}