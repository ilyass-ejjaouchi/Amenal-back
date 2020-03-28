package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.QualificationOuvrierRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
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
	OuvrierDesignationRepository ouvrierDesignationRepository;

	@Autowired
	QualificationOuvrierRepository qualificationOuvrierRepository;

	@Autowired
	OuvrierMapper ouvrierMapper;

	@Autowired
	ProjetMetier projetMetier;

	public Ouvrier ajouterOuvrier(OuvrierCommande ouvrierCmd) {

		/*
		 * exception...
		 */
		QualificationOuvrier qualification = qualificationOuvrierRepository.findByCode(ouvrierCmd.getQualification());

		if (qualification == null)
			throw new NotFoundException(
					"La qualification dont l' id [ " + ouvrierCmd.getQualification() + " ] est introuvable!");

		Ouvrier ouvrier = ouvrierMapper.toEntity(ouvrierCmd);
		ouvrier.setQualification(qualification);

		return ouvrierRepository.save(ouvrier);

	}

	public Ouvrier modifierOuvrier(OuvrierCommande ouvrierCmd, Integer id) {

		Ouvrier ouvrier = ouvrierMapper.toEntity(ouvrierCmd);

		if (!ouvrierRepository.findById(id).isPresent())
			throw new NotFoundException("cet ouvrier est inexistant");

		QualificationOuvrier qualification = qualificationOuvrierRepository
				.findByCode(ouvrier.getQualification().getCode());

		if (qualification == null)
			throw new NotFoundException(
					"La qualification [ " + ouvrier.getQualification().getCode() + " ] est introuvable!");

		ouvrier.setId(id);
		ouvrier.setQualification(qualification);

		Ouvrier ouv = ouvrierRepository.save(ouvrier);

		List<OuvrierDesignation> Dss = ouvrierDesignationRepository.findDesignationByOuvrierIDAndFicheNotValid(id);
		if (!Dss.isEmpty())
			Dss.forEach(d -> {
				d.setCin(ouvrier.getCin());
				d.setNom(ouvrier.getNom() + " " + ouvrier.getPrenom());
				d.setQualification(ouvrier.getQualification().getCode());
			});

		return ouv;

	}

	public List<OuvrierPresentation> ListerOuvriers() {

		return ouvrierRepository.findAll().stream().map(o -> ouvrierMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void SupprimerOuvrier(Integer OuvId) {

		Optional<Ouvrier> ouv = this.ouvrierRepository.findById(OuvId);
		if (!ouv.isPresent())
			throw (new NotFoundException("L'ouvrier ["+OuvId+"] introuvable"));
		
		List<OuvrierDesignation> Dss = ouvrierDesignationRepository.findDesignationByOuvrierIDAndFicheNotValid(OuvId);
		
		if(!Dss.isEmpty())
			throw new BadRequestException("L'ouvrier [ "+ ouv.get().getNom() + " "+  ouv.get().getPrenom() + " ] est deja associer a des fiche non valide");

		List<OuvrierDesignation> ouvDSs = ouvrierDesignationRepository.findDesignationByOuvrierID(OuvId);
		ouvDSs.forEach(ouvDs -> {
			if (ouvDs != null)
				ouvDs.setOuvrier(null);
		});

		List<Projet> ps = projetRepository.findProjetByOuvrierID(OuvId);

		if (!ps.isEmpty())
			ps.forEach(p -> projetMetier.AssocierOuvrierProjet(OuvId, p.getId()));

		this.ouvrierRepository.deleteById(OuvId);

	}

}
