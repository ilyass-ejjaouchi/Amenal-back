package org.amenal.metier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Projet;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.FicheTypeEnum;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetMetier {

	@Autowired
	ProjetRepository projetDao;
	@Autowired
	OuvrierFicheRepository OuvFicheDao;
	private List<FicheTypeEnum> types;

	public Projet addProjet(Projet projet , List<String> fichetype ) {
		
		if( projetDao.findByTitre(projet.getTitre()) == null )
			throw new BadRequestException("Le projet "+projet.getTitre()+" déjà existant.");
		
		types = new ArrayList<FicheTypeEnum>();
		fichetype.forEach(type->{
			types.add(FicheTypeEnum.fromCode(type));
		});
		projet.setFichierTypes(types);
		
		projet.setFichiers(CreateFiche(fichetype , projet));
		return projetDao.save(projet);
	}

	public List<Projet> ListProjet(){
		
		return projetDao.findAll();
		
	}
	
	private static List<Fiche> CreateFiche(List<String> ficheTypes , Projet p) {

		List<Fiche> fiches = new ArrayList<Fiche>();

		ficheTypes.forEach(type -> {
			switch (FicheTypeEnum.fromCode(type)) {
			case LOC:

				break;
			case MOO:
				OuvrierFiche OuvFiche = new OuvrierFiche();
				OuvFiche.setDate(new Date());
				OuvFiche.setProjet(p);
				
				fiches.add(OuvFiche);
				break;

			default:
				break;
			}
		});

		return fiches;

	}

}
