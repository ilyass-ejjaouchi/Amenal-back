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

class AmenalBackApplication {

	public static void main(String[] args) {
		//SpringApplication.run(AmenalBackApplication.class, args);
		
		
		System.out.println("ggg");
	}

	
}
