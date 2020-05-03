package org.amenal.metier.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.amenal.dao.ProjetAppUserRoleAssoRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.security.AppRoleRepository;
import org.amenal.dao.security.AppUserRepository;
import org.amenal.entities.Projet;
import org.amenal.entities.security.AppRole;
import org.amenal.entities.security.AppUser;
import org.amenal.entities.security.ProjetAppUserRoleAsso;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.RegistrationForm;
import org.amenal.rest.commande.UserRoleProjetCommande;
import org.amenal.rest.representation.RoleProjetPresentation;
import org.amenal.rest.representation.UserPresentation;
import org.amenal.rest.representation.UserProjetRolePresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AccountMetier {

	@Autowired
	private AppUserRepository userRepository;
	@Autowired
	private AppRoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private ProjetRepository projetRepository;
	@Autowired
	private ProjetAppUserRoleAssoRepository projetAppUserRoleAssoRepository;

	public List<UserProjetRolePresentation> ListerUserWithProjet() {

		List<AppUser> users = userRepository.listerUserWithRole();
		List<UserProjetRolePresentation> usersPrs = new ArrayList<UserProjetRolePresentation>();

		for (AppUser u : users) {

			
			System.out.println();
			System.out.println(u.getUsername());
			
			UserProjetRolePresentation user = new UserProjetRolePresentation();
			user.setUsername(u.getUsername());
			List<RoleProjetPresentation> rprs = new ArrayList<RoleProjetPresentation>();
			
			for (ProjetAppUserRoleAsso r : u.getRoles()) {
				System.out.println(r.getRole().getRole());
				RoleProjetPresentation rr = new RoleProjetPresentation();
				rr.setId(r.getId());
				rr.setProjet(r.getProjet().getAbreveation());
				rr.setRole(r.getRole().getRole());
				rprs.add(rr);
			}
			
			user.setRoles(rprs);
			usersPrs.add(user);
		}

		return usersPrs;    
	}

	public void supprimerUser(Integer id) {
		userRepository.deleteById(id);
	}

	public void AddUser(RegistrationForm data) {
		String username = data.getUsername();
		AppUser user = userRepository.findByUsername(username);
		if (user != null)
			throw new BadRequestException("This user already exists, Try with an other username");
		String password = data.getPassword();

		AppUser u = new AppUser();
		u.setUsername(username);
		String rndm = "";
		for (int i = 0; i < password.length(); i++) {
			rndm = rndm + ".";
		}
		u.setPasswordDoted(rndm);

		u.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(u);

	}

	public void grantRoleOfProjetToUser(UserRoleProjetCommande cmd) {

		ProjetAppUserRoleAsso asso = projetAppUserRoleAssoRepository
				.findByUserUsernameAndProjetIdAndRoleRole(cmd.getUsername(), cmd.getPid(), cmd.getRole());

		if (asso == null) {
			asso = new ProjetAppUserRoleAsso();
			Optional<Projet> p = projetRepository.findById(cmd.getPid());
			AppUser user = userRepository.findByUsername(cmd.getUsername());
			AppRole role = roleRepository.findByRole(cmd.getRole());
			asso.setProjet(p.get());
			asso.setUser(user);
			asso.setRole(role);
			System.out.println(asso.getUser().getUsername()+"   "+asso.getRole().getRole());
			projetAppUserRoleAssoRepository.save(asso);
		}
	}

	public void UpdategrantedRoleOfProjetToUser(UserRoleProjetCommande cmd, Integer AssId) {

		Optional<Projet> p = projetRepository.findById(cmd.getPid());
		AppUser user = userRepository.findByUsername(cmd.getUsername());
		AppRole role = roleRepository.findByRole(cmd.getRole());
		ProjetAppUserRoleAsso asso = new ProjetAppUserRoleAsso();
		asso.setId(AssId);
		asso.setProjet(p.get());
		asso.setUser(user);
		asso.setRole(role);

	}

	public void DeletegrantedRoleOfProjetToUser(Integer AssId) {

		projetAppUserRoleAssoRepository.deleteById(AssId);

	}

	public List<UserPresentation> ListUser() {

		return this.userRepository.findAll().stream().map(u -> {
			return (new UserPresentation(u.getId(), u.getUsername(), u.getPasswordDoted(), null, u.isRoot()));
		}).collect(Collectors.toList());
	}

	public void UpdateUser(String username, String password, Integer id) {

		Optional<AppUser> u = userRepository.findById(id);

		if (!u.isPresent())
			throw new NotFoundException("L' utilisateur est introuvable!");

		u.get().setUsername(username);

		String rndm = "";
		for (int i = 0; i < password.length(); i++) {
			rndm = rndm + ".";
		}
		u.get().setPasswordDoted(rndm);

		u.get().setPassword(bCryptPasswordEncoder.encode(password));

	}

}
