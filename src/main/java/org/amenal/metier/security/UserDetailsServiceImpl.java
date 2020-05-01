package org.amenal.metier.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.amenal.config.security.dto.ProjetAuthority;
import org.amenal.config.security.dto.UserDto;
import org.amenal.dao.security.AppUserRepository;
import org.amenal.entities.security.AppUser;
import org.amenal.entities.security.ProjetAppUserRoleAsso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private AppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser u = userRepository.findByUsername(username);

		if (u == null)
			throw new UsernameNotFoundException(username);
		List<ProjetAuthority> authorities = new ArrayList<ProjetAuthority>();

		for (ProjetAppUserRoleAsso ass : u.getRoles())
			authorities.add(new ProjetAuthority(ass.getProjet().getId(), ass.getRole().getRole()));

		UserDto uu = new UserDto(u.getUsername(), u.getPassword(),u.isRoot() ,authorities);

		return uu;
	}
}