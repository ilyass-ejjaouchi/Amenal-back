package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.OuvrierDesignationCommande;
import org.amenal.rest.mapper.OuvrierDesignationMapper;
import org.amenal.rest.representation.OuvrierDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OuvrierFicheMetier {

	@Autowired
	OuvrierDesignationMapper ouvrierDesignationMapper;

	@Autowired
	
	OuvrierDesignationRepository ouvrierDesignationRepository;

	@Autowired
	OuvrierRepository ouvrierRepository;
	
	
	public OuvrierDesignation addLigneDesignation(OuvrierDesignationCommande dsCmd) {
		
		

		OuvrierDesignation des = ouvrierDesignationMapper.toEntity(dsCmd);
		
		Optional<Ouvrier> ouv = ouvrierRepository.findById(des.getOuvrier().getId());
		
		if(!ouv.isPresent())
			throw new BadRequestException("Cet ouvrier n'existe pas!");
		
		des.setCin(ouv.get().getCin());
		des.setNom(ouv.get().getNom());
		des.setQualification(ouv.get().getQualification().getCode());

		
		
		if( des.getJour() * 9 + des.getHSup() < 9) {
			
			throw new BadRequestException("information incompatible");
			
		}
		
		
		return ouvrierDesignationRepository.save(des);

	}

	public List<OuvrierDesignationPresentation> ListOuvrierDesignation() {
		
		return ouvrierDesignationRepository.findAll().stream().map(o -> ouvrierDesignationMapper.toRepresentation(o)).collect(Collectors.toList());


	}
	
	
	
	
	
	
	

}
