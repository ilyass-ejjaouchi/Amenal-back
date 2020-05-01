package org.amenal.entities.security;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.amenal.entities.Projet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="PROJET_USER_ROLE")
@NoArgsConstructor
@Getter
@Setter
public class ProjetAppUserRoleAsso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private AppUser user;

	@ManyToOne
	private Projet projet;

	@ManyToOne
	private AppRole role;

}
