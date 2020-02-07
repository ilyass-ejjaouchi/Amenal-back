package org.amenal;

import java.util.ArrayList;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.entities.fiches.FicheTypeEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = { "org.amenal" })
class AmenalBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmenalBackApplication.class, args);
	}

	@Bean
	CommandLineRunner start(ProjetRepository repo, OuvrierDesignationRepository orepo) {

		return args -> {
			System.out.println("start..............");
			ArrayList<FicheTypeEnum> types = new ArrayList<FicheTypeEnum>();
			types.add(FicheTypeEnum.MOO);

			Projet p = new Projet();
			p.setId(1);
			p.setTitre("title1");
			p.setFichierTypes(types);
			repo.save(p);

			Projet p2 = new Projet();
			p2.setId(2);
			p2.setTitre("title2");
			p2.setFichierTypes(types);

			repo.save(p2);

			Ouvrier o = new Ouvrier();
			o.setId(1);

			OuvrierDesignation l = new OuvrierDesignation();
			l.setCin("eeeeeeeeeeee");

			orepo.save(l);

		};
	}

}
