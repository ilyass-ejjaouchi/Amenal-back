package org.amenal.metier;

import java.util.List;
import java.util.stream.Collectors;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.rest.commande.OuvrierDesignationCommande;
import org.amenal.rest.mapper.OuvrierDesignationMapper;
import org.amenal.rest.representation.OuvrierDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OuvrierFicheMetier {

	@Autowired
	OuvrierDesignationMapper ouvrierDesignationMapper;

	@Autowired
	OuvrierDesignationRepository ouvrierDesignationRepository;

	public OuvrierDesignation addLigneDesignation(OuvrierDesignationCommande dsCmd) {

		OuvrierDesignation des = ouvrierDesignationMapper.toEntity(dsCmd);
		/*
		 * exeption...
		 */
		
		System.out.println();

		return ouvrierDesignationRepository.save(des);

	}

	public List<OuvrierDesignationPresentation> ListOuvrierDesignation() {
		
		return ouvrierDesignationRepository.findAll().stream().map(o -> ouvrierDesignationMapper.toRepresentation(o)).collect(Collectors.toList());


	}
	
	
	
	
	
	
	

}
