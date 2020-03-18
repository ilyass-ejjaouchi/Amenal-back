package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.LocationAssoRepository;
import org.amenal.dao.MaterielRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Materiel;
import org.amenal.entities.Projet;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.mapper.MaterielMapper;
import org.amenal.rest.representation.MaterielPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MaterielMetier {

	@Autowired
	MaterielRepository materielRepository;

	@Autowired
	MaterielMapper materielMapper;
	
	@Autowired
	LocationAssoRepository locationAssoRepository;
	
	@Autowired
	ProjetRepository projetRepository;

	public Materiel ajouterMateriel(MaterielCommande materielCmd) {

		Materiel materiel = materielMapper.toEntity(materielCmd);
		return this.materielRepository.save(materiel);

	}
	
	public List<MaterielPresentation> ListerMaterielByProjet(Integer projetID){
		
		Optional<Projet> projet = projetRepository.findById(projetID);
		
		if (!projet.isPresent())
			throw new NotFoundException("le projet [" + projetID + "] est inexistant");
		
		return this.locationAssoRepository.findMaterilByProjet(projet.get()).stream().map(o -> materielMapper.toRepresentation(o))
				.collect(Collectors.toList());
	}

	public List<MaterielPresentation> ListerMateriel() {

		return this.materielRepository.findAll().stream().map(o -> materielMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}
	public void modifierMateriel(MaterielCommande matCmd,Integer matID){
		
		Optional<Materiel> mtr = materielRepository.findById(matID);

		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + matID + "] est inexistant");
		
		matCmd.setId(matID);
		
		this.materielRepository.save(materielMapper.toEntity(matCmd));
	}
	
	public void SupprimerMateriel(Integer matID , Boolean ctn) {
		
		Optional<Materiel> mtr = materielRepository.findById(matID);

		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + matID + "] est inexistant");
		List<LocationAsso> locs = locationAssoRepository.findByMateriel(mtr.get());
		
		if(!locs.isEmpty()) {
			if(ctn) {
				
				locs.forEach(l->{
					locationAssoRepository.delete(l);
				});
				materielRepository.delete(mtr.get());
				
			}else {
				String fours="";
				for (LocationAsso l : locs) {
					
					fours+=l.getFourniseur().getLibelle()+" , ";

					
				}
				throw new BadRequestException("Ce materiel est deja associer au fournisseur: "+fours);
			}
		
		}else {
			materielRepository.delete(mtr.get());

		}
		
		
	}

}
