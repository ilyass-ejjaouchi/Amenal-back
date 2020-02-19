package org.amenal;

import java.lang.module.FindException;
import java.util.ArrayList;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.FicheTypeEnum;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.metier.ProjetMetier;
import org.amenal.rest.commande.ProjetCommande;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;



@SpringBootApplication
@ComponentScan(basePackages = { "org.amenal" })

class AmenalBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmenalBackApplication.class, args);
		
		
		System.out.println("ggg");
	}

	@Bean
	@Transactional
	CommandLineRunner start(ProjetMetier repo, OuvrierDesignationRepository orepo , OuvrierFicheRepository Orepo) {

		return args -> {
			
			
			
			
			
			
			
			/*
			 * System.out.println("start.............."); ArrayList<String> types = new
			 * ArrayList<String>(); types.add(FicheTypeEnum.MOO.getCode());
			 * 
			 * 
			 * 
			 * Ouvrier o = new Ouvrier(); o.setId(1);
			 * 
			 * OuvrierDesignation l = new OuvrierDesignation(); l.setCin("eeeeeeeeeeee");
			 * 
			 * orepo.save(l);
			 * 
			 * ProjetCommande p_cmd = new ProjetCommande(); p_cmd.setTitre("title1");
			 * p_cmd.setFichierTypes(types); pm.addProjet(p_cmd);
			 */
				


		};
	}

}
