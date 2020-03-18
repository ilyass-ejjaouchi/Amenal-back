package org.amenal.metier;

import org.amenal.dao.UniteRepository;
import org.amenal.entities.Unite;
import org.amenal.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UniteMetier {
	
	@Autowired
	UniteRepository uniteRepository;
	
	
	public void AddUnite(String name) {
		
		Unite u = new Unite();
		if(name.trim() == "")
			throw new BadRequestException("l'unite ne doit pas etre vide! ");
		u.setUnite(name);
		uniteRepository.save(u);
	}

}
