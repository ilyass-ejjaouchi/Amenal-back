package org.amenal;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.dao.security.AppRoleRepository;
import org.amenal.dao.security.AppUserRepository;
import org.amenal.entities.security.AppRole;
import org.amenal.entities.security.AppUser;
import org.amenal.metier.ProjetMetier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = { "org.amenal" })

public class AmenalBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmenalBackApplication.class, args);

		System.out.println("ggg");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Transactional
	CommandLineRunner start(ProjetMetier repo, AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {

		return args -> {

		/*	AppRole admin = new AppRole();
			admin.setRole("ADMIN");
			appRoleRepository.save(admin);

			AppRole user = new AppRole();
			admin.setRole("USER");
			appRoleRepository.save(user);

			AppUser rootUser = new AppUser();
			rootUser.setRoot(true);
			rootUser.setUsername("root");
			rootUser.setPassword(bCryptPasswordEncoder.encode("root"));

			appUserRepository.save(rootUser);*/

		};
	}

}
