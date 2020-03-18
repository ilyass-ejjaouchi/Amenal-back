package org.amenal.metier;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.LocationFicheRepository;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.LocationDesignationCommande;
import org.amenal.rest.mapper.LocationDesignationMapper;
import org.amenal.rest.representation.LocationDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationFicheMetier {
	
	@Autowired
	private LocationFicheRepository locationFicheRepository;
	@Autowired
	private LocationDesignationRepository locationDesignationRepository;
	@Autowired
	private LocationDesignationMapper locationDesignationMapper;
		
		
	

	public LocationDesignation addLigneDesignation(
			@Valid LocationDesignationCommande dsCmd) {
		// TODO Auto-generated method stub
		LocationDesignation locDs = locationDesignationMapper.toEntity(dsCmd);
		Integer ficheId = locDs.getLocationFiche().getId();
		Optional<LocationFiche> fiche =  locationFicheRepository.findById(ficheId);
		if(!fiche.isPresent())
			throw new NotFoundException("La fiche [ "+ficheId+" ] est introuvable !");
		
		return locationDesignationRepository.save(locDs);
	}
	public List<LocationDesignationPresentation> ListLocationDesignation() {
		// TODO Auto-generated method stub
		return null;
	}
	public void SupprimerLocationDesignation(Integer ouvId) {
		// TODO Auto-generated method stub
		
	}
	public void updateLigneDesignation(@Valid LocationDesignationCommande dsCmd, Integer locDsId) {
		// TODO Auto-generated method stub
		Optional<LocationDesignation> Ds = locationDesignationRepository.findById(locDsId);
		if(!Ds.isPresent())
			throw new NotFoundException("La ligne [ "+locDsId+" ] est introuvable !");
		
		LocationDesignation locDs = locationDesignationMapper.toEntity(dsCmd);
		locDs.setId(locDsId);
		Integer ficheId = locDs.getLocationFiche().getId();
		Optional<LocationFiche> fiche =  locationFicheRepository.findById(ficheId);
		if(!fiche.isPresent())
			throw new NotFoundException("La fiche [ "+ficheId+" ] est introuvable !");
		
		
		locationDesignationRepository.save(locDs);
		
	}
	

}
