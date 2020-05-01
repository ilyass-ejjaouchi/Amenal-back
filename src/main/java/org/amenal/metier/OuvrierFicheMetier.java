package org.amenal.metier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.LivraisonDesignationRepository;
import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierFicheRepository;
import org.amenal.dao.OuvrierRepository;
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
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
	ReceptionDesignationRepository receptionDesignationRepository;

	@Autowired
	OuvrierRepository ouvrierRepository;

	@Autowired
	ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	StockMetier stockMetier;

	@Autowired
	OuvrierFicheRepository ouvrierFicheRepository;

	public OuvrierDesignation addLigneDesignation(OuvrierDesignationCommande dsCmd) {

		OuvrierDesignation des = ouvrierDesignationMapper.toEntity(dsCmd);

		Optional<OuvrierFiche> ouvFiche = ouvrierFicheRepository.findById(des.getFiche().getId());
		if (!ouvFiche.isPresent()) {
			throw new NotFoundException("Fiche introuvable");
		}

		if (ouvFiche.get().getIsValidated())
			throw new BadRequestException("Cette fiche est deja valider!");

		Optional<Ouvrier> ouv = ouvrierRepository.findById(des.getOuvrier().getId());

		if (!ouv.isPresent())
			throw new BadRequestException("Cet ouvrier n'existe pas!");

		des.setCin(ouv.get().getCin());
		des.setNom(ouv.get().getNom() + " " + ouv.get().getPrenom());
		des.setQualification(ouv.get().getQualification().getCode());
		des.setOuvrier(ouv.get());

		/*
		 * if( des.getJour() * 9 + des.getHSup() < 9) {
		 * 
		 * throw new BadRequestException("information incompatible");
		 * 
		 * }
		 */
		des = ouvrierDesignationRepository.save(des);

		ReceptionDesignation rec = receptionDesignationRepository.FindByCategorieAndFicheNotValid("MAIN D'OEUVRE",
				ouvFiche.get().getProjet());

		if (rec == null) {
			rec = new ReceptionDesignation();
			
			rec.setCategorie("MAIN D'OEUVRE");
			rec.getOuvrierDesignations().add(des);

			ReceptionFiche f = receptionFicheRepository.findByDateAndProjet(ouvFiche.get().getDate(),ouvFiche.get().getProjet());

			rec.setFiche(f);

			receptionDesignationRepository.save(rec);
			des.setReceptionDesignationOuv(rec);

		} else {
			rec.getOuvrierDesignations().add(des);
			des.setReceptionDesignationOuv(rec);

		}

		return des;

	}

	public OuvrierDesignation updateLigneDesignation(OuvrierDesignationCommande dsCmd, Integer OuvDsId) {

		OuvrierDesignation des = ouvrierDesignationMapper.toEntity(dsCmd);
		
		Optional<OuvrierFiche> ouvFiche = ouvrierFicheRepository.findById(des.getFiche().getId());
		if (!ouvFiche.isPresent()) {
			throw new NotFoundException("Fiche introuvable");
		}

		if (ouvFiche.get().getIsValidated())
			throw new BadRequestException("Cette fiche est deja valider!");
		
		if (!this.ouvrierDesignationRepository.findById(OuvDsId).isPresent())
			throw (new NotFoundException("ouvirer introuvabe"));

		Optional<OuvrierDesignation> odDs = this.ouvrierDesignationRepository.findById(OuvDsId);

		if (odDs.get().getFiche().getIsValidated()) {
			throw (new BadRequestException("La fiche associer a cette ligne est deja validé!"));

		}

		des.setId(OuvDsId);

		if (des.getOuvrier() != null) {

			Optional<Ouvrier> ouv = ouvrierRepository.findById(des.getOuvrier().getId());
			if (ouv.isPresent())
				des.setOuvrier(ouv.get());
			else
				des.setOuvrier(null);

		}

		
		ReceptionDesignation rec = receptionDesignationRepository.FindByCategorieAndFicheNotValid("MAIN D'OEUVRE",
				ouvFiche.get().getProjet());

		if (rec == null) {
			rec = new ReceptionDesignation();
			rec.setCategorie("MAIN D'OEUVRE");
			rec.getOuvrierDesignations().add(des);

			ReceptionFiche f = receptionFicheRepository.findByDateAndProjet(ouvFiche.get().getDate(),ouvFiche.get().getProjet());

			rec.setFiche(f);

			receptionDesignationRepository.save(rec);
			des.setReceptionDesignationOuv(rec);

		} else {
			rec.getOuvrierDesignations().add(des);
			des.setReceptionDesignationOuv(rec);
		}

		return ouvrierDesignationRepository.save(des);

	}

	public List<OuvrierDesignationPresentation> ListOuvrierDesignation() {

		return ouvrierDesignationRepository.findAll().stream().map(o -> ouvrierDesignationMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void SupprimerOuvrierDesignation(Integer OuvDsId) {
		Optional<OuvrierDesignation> odDs = this.ouvrierDesignationRepository.findById(OuvDsId);

		if (!odDs.isPresent())
			throw (new NotFoundException("designation introuvable!"));
		if (odDs.get().getFiche().getIsValidated()) {
			throw (new BadRequestException("La fiche associer a cette ligne est deja validé!"));
		}

		if (odDs.get().getReceptionDesignationOuv().getOuvrierDesignations().size() == 1) {
			receptionDesignationRepository.delete(odDs.get().getReceptionDesignationOuv());
		} else
			this.ouvrierDesignationRepository.deleteById(OuvDsId);

	}

	public void ValiderFicheOuvrier(Integer ficheId) {
		Optional<OuvrierFiche> ouvFiche = ouvrierFicheRepository.findById(ficheId);
		if (!ouvFiche.isPresent()) {
			throw new NotFoundException("fiche introuvable");
		}
		for (OuvrierDesignation ds : ouvFiche.get().getOuvrierDesignation())
			if (!ds.getValid())
				throw new BadRequestException("La fiche ne peut pas etre valider !");

		ouvFiche.get().setIsValidated(true);
		/*
		 * for (LocalDate date = ouvFiche.get().getDate();
		 * date.isBefore(LocalDate.now()); date = date.plusDays(1)) { OuvrierFiche fiche
		 * = new OuvrierFiche(); fiche.setProjet(ouvFiche.get().getProjet());
		 * fiche.setDate(date);
		 * 
		 * ouvrierFicheRepository.save(fiche);
		 * 
		 * }
		 */

	}

}
