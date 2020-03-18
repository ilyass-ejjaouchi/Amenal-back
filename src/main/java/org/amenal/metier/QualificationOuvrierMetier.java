package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.QualificationOuvrierRepository;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QualificationOuvrierMetier {
	
	@Autowired
	QualificationOuvrierRepository qualificationOuvrierRepository;
	
	
	public Integer AjouterQualificationOuvrier(String Qualification){
		
		 QualificationOuvrier qual = qualificationOuvrierRepository.findByCode(Qualification); 
		 if(qual == null) 
			 throw new NotFoundException("La qualification[ "+Qualification+" ] est deja existante!");

		QualificationOuvrier qualif = new QualificationOuvrier();
		qualif.setCode(Qualification);
		
		qualif = qualificationOuvrierRepository.save(qualif);
		
		return 1;
	}
	
	public List<String> ListerQualificationOuvrier(){	
		return qualificationOuvrierRepository.findAll().stream().map(o -> o.getCode())
				.collect(Collectors.toList()); 
	}
	
	public void SupprimerQualificationOuvrier(String code){	
		
		 QualificationOuvrier qual = qualificationOuvrierRepository.findByCode(code); 
		 if(qual == null) 
			 throw new NotFoundException("La qualification dont l' id [ "+code+" ] est introuvable!");
		 
		 qualificationOuvrierRepository.delete(qual);
	}

}
