package org.amenal.metier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.amenal.dao.ActiviteDesignationRepository;
import org.amenal.dao.LotRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Lot;
import org.amenal.entities.Projet;
import org.amenal.entities.SousLot;
import org.amenal.entities.designations.ActiviteDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.mapper.LotMapper;
import org.amenal.rest.representation.LotPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class LotMetier {

	@Autowired
	LotRepository lotRepository;

	@Autowired
	LotMapper lotMapper;

	@Autowired
	ActiviteDesignationRepository activiteDesignationRepository;

	@Autowired
	ProjetRepository projetRepository;

	public void AssoLotToProjet(Integer lotId, Integer projetId) {

		Optional<Lot> lot = lotRepository.findById(lotId);
		Boolean lotIsAsso = false;
		Optional<Projet> projet = projetRepository.findById(projetId);

		List<ActiviteDesignation> ss = activiteDesignationRepository
				.findSousLotDesignationByProjetIdWhereFicheNonValid(lotId, projetId);

		if (!ss.isEmpty())
			throw new BadRequestException(
					"Vous ne pouvez pas supprimer ce lot  , il' est associé a une fiche non valide!");

		for (SousLot sl : lot.get().getSousLots()) {
			List<Integer> projetIds = sl.getProjets().stream().map(oo -> oo.getId()).collect(Collectors.toList());
			if (projetIds.contains(projetId)) {
				lotIsAsso = true;
				break;
			}
		}

		if (lotIsAsso) {
			for (SousLot sl : lot.get().getSousLots()) {
				List<Integer> projetIds = sl.getProjets().stream().map(oo -> oo.getId()).collect(Collectors.toList());
				if (projetIds.contains(projetId)) {
					sl.getProjets().forEach(p -> {
						p.getSousLots().remove(sl);
					});
				}
			}
		} else {
			for (SousLot sl : lot.get().getSousLots()) {
				List<Integer> projetIds = sl.getProjets().stream().map(oo -> oo.getId()).collect(Collectors.toList());
				if (!projetIds.contains(projetId)) {
					projet.get().getSousLots().add(sl);
				}
			}

		}

	}

	public List<LotPresentation> listLot(Integer projetId) {

		return lotRepository.findAll().stream().map(o -> {
			Boolean lotIsAsso = false;
			for (SousLot sl : o.getSousLots()) {
				List<Integer> projetIds = sl.getProjets().stream().map(oo -> oo.getId()).collect(Collectors.toList());
				if (projetIds.contains(projetId)) {
					lotIsAsso = true;
					sl.setIsAsso(true);
				}

			}
			LotPresentation lot = lotMapper.toRepresentation(o);

			lot.setIsAsso(lotIsAsso);

			return lot;
		}).collect(Collectors.toList());

	}

	public void AddLot(String lotDesignation) {

		Lot ll = lotRepository.findByDesignation(lotDesignation);

		if (ll != null)
			throw new BadRequestException("Ce lot est deja existant!");

		Lot lot = new Lot();

		lot.setDesignation(lotDesignation);

		lotRepository.save(lot);

	}

	public void UpdateLot(String lotDesignation, Integer id) {

		Lot ll = lotRepository.findByDesignation(lotDesignation);

		if (ll != null)
			throw new BadRequestException("Ce lot est deja existant!");

		Lot lot = new Lot();

		lot.setDesignation(lotDesignation);

		lot.setId(id);

		lotRepository.save(lot);

	}

	public void DeleteLot(Integer id) {

		List<ActiviteDesignation> ss = activiteDesignationRepository.findLotDesignationWhereFicheNonValid(id);

		if (!ss.isEmpty())
			throw new BadRequestException(
					"Vous ne pouvez pas supprimer ce lot , il'est associé a une fiche non valide!");

		lotRepository.deleteById(id);

	}

}
