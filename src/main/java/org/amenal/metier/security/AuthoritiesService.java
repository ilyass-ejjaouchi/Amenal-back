package org.amenal.metier.security;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.config.security.dto.AppUserAuthenticationToken;
import org.amenal.config.security.dto.ProjetAuthority;
import org.amenal.dao.DesignationRepository;
import org.amenal.dao.FicheRepository;
import org.amenal.entities.designations.Designation;
import org.amenal.entities.fiches.Fiche;
import org.amenal.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthoritiesService {

	@Autowired
	DesignationRepository designationRepository;

	@Autowired
	FicheRepository<Fiche> FicheRepository;

	public Boolean hasAuthority(Integer pid, String role) {

		AppUserAuthenticationToken auth = (AppUserAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();


		if (auth.isRoot()) {
			return true;
		}

		Map<Integer, List<ProjetAuthority>> result = auth.getAuthorities().stream().map(o -> (ProjetAuthority) o)
				.collect(Collectors.groupingBy(ProjetAuthority::getProjetId));

		for (ProjetAuthority a : result.get((Integer) pid)) {
			if ((a.getProjetId() == pid && a.getAuthority().equals(role)))
				return true;
		}

		return false;

	}

	public Boolean hasAuthorityDs(Integer dsId, String authority) {

		Optional<Designation> ds = designationRepository.findById(dsId);

		if (!ds.isPresent())
			throw new NotFoundException("Cette designation est introuvable");

		Integer pid = ds.get().getFiche().getProjet().getId();

		return this.hasAuthority(pid, authority);
	}

	public Boolean hasAuthorityFiche(Integer dsId, String authority) {

		Optional<Fiche> fiche = FicheRepository.findById(dsId);

		Integer pid = fiche.get().getProjet().getId();

		return this.hasAuthority(pid, authority);
	}

	public Boolean hasAuthority(String authority) {
		AppUserAuthenticationToken auth = (AppUserAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();

		if (auth.isRoot())
			return true;

		List<String> result = auth.getAuthorities().stream().map(o -> ((ProjetAuthority) o).getAuthority())
				.collect(Collectors.toList());

		if (result.contains(authority))
			return true;
		return false;

	}

	public Boolean isRoot() {
		AppUserAuthenticationToken auth = (AppUserAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();

		return auth.isRoot();
	}

}
