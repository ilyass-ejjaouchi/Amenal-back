package org.amenal;


import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.metier.ProjetMetier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;



@SpringBootApplication
@ComponentScan(basePackages = { "org.amenal" })

public class AmenalBackApplication {

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
