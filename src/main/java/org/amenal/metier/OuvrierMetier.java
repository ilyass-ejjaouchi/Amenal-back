package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.OuvrierRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.mapper.OuvrierMapper;
import org.amenal.rest.representation.OuvrierPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OuvrierMetier {

	@Autowired
	OuvrierRepository ouvrierRepository;

	@Autowired
	ProjetRepository projetRepository;

	
	@Autowired
	OuvrierMapper ouvrierMapper;
	

	public Ouvrier ajouterOuvrier(OuvrierCommande ouvrierCmd) {

		/*
		 * exception...
		 */
		Ouvrier ouvrier = ouvrierMapper.toEntity(ouvrierCmd);

		return ouvrierRepository.save(ouvrier);

	}

	public Ouvrier modifierOuvrier(Ouvrier ouvrier) {

		/*
		 * exception...
		 */

		return ouvrierRepository.save(ouvrier);

	}

	
	public List<OuvrierPresentation> ListerOuvriers( ) {

		return ouvrierRepository.findAll().stream().map(o -> ouvrierMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	

}
