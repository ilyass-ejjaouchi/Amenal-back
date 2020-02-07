package org.amenal.metier;


import org.amenal.dao.OuvrierRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.mapper.OuvrierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OuvrierMetier {
	
	@Autowired
	OuvrierRepository ouvrierRepository;
	
	@Autowired
	OuvrierMapper ouvrierMapper;
	
	
	public Ouvrier ajouterOuvrier(OuvrierCommande ouvrierCmd) {
		
		/*
		 * exception...
		 * */
		Ouvrier ouvrier = ouvrierMapper.toEntity(ouvrierCmd);
		
		
		return ouvrierRepository.save(ouvrier);
		
	}
	
	public Ouvrier modifierOuvrier(Ouvrier ouvrier) {
		
		/*
		 * exception...
		 * */
		
		
		return ouvrierRepository.save(ouvrier);
		
	}

}
