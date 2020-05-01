package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.amenal.dao.AccidentDesignationRepository;
import org.amenal.dao.AccidentFicherepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.FicheBesoinRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.dao.VisiteurFicheRepository;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.AccidentDesignation;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.entities.fiches.AccidentFiche;
import org.amenal.entities.fiches.BesoinFiche;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.entities.fiches.VisiteurFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.AccidentDesignationCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccidentFicheMetier {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	AccidentFicherepository accidentFicherepository;

	@Autowired
	AccidentDesignationRepository accidentDesignationRepository;

	@Autowired
	ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	DocFicheRepository docFicheRepository;

	@Autowired
	FicheBesoinRepository ficheBesoinRepository;

	@Autowired
	VisiteurFicheRepository visiteurFicheRepository;

	@Autowired
	StockMetier stockMetier;

	@Autowired
	private ProjetRepository projetRepository;

	public void AddAccidentDesignation(AccidentDesignationCommande cmd) {

		Optional<AccidentFiche> accFiche = this.accidentFicherepository.findById(cmd.getFicheID());

		if (!accFiche.isPresent())
			throw new NotFoundException("La fiche est introuvable!");

		AccidentDesignation acc = new AccidentDesignation();
		acc.setObjet(cmd.getObjet().toUpperCase());
		acc.setHeure(cmd.getHeure());
		acc.setFiche(accFiche.get());
		accidentDesignationRepository.save(acc);

	}

	public void UpdateAccidentDesignation(AccidentDesignationCommande cmd, Integer id) {

		Optional<AccidentFiche> accFiche = this.accidentFicherepository.findById(cmd.getFicheID());

		if (!accFiche.isPresent())
			throw new NotFoundException("La fiche est introuvable!");

		AccidentDesignation acc = new AccidentDesignation();
		acc.setObjet(cmd.getObjet().toUpperCase());
		acc.setHeure(cmd.getHeure());
		acc.setFiche(accFiche.get());
		acc.setId(id);
		accidentDesignationRepository.save(acc);
	}

	public void deleteAccidentDesignation(Integer id) {

		accidentDesignationRepository.deleteById(id);
	}

	public void validerFicheAccident(Integer id) {

		Optional<AccidentFiche> fiche = accidentFicherepository.findById(id);
		if (!fiche.get().getIsValidated()) {
			Projet p = fiche.get().getProjet();
			fiche.get().setIsValidated(true);

			LivraisonFiche livF = livraisonFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			DocFiche docf = docFicheRepository.findByDateAndProjet(fiche.get().getDate(), fiche.get().getProjet());
			VisiteurFiche ficheV = visiteurFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			BesoinFiche bsnFiche = ficheBesoinRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());

			if (livF.getIsValidated() && docf.getIsValidated() && ficheV.getIsValidated()
					&& bsnFiche.getIsValidated()) {

				List<Fiche> fiches = new ArrayList<Fiche>();

				LocalDate date = fiche.get().getDate().plusDays(1);
				stockMetier.validerFicheStock(fiche.get().getDate(), fiche.get().getProjet());

				/* OUVRIER */
				OuvrierFiche ouvFiche = new OuvrierFiche();
				ouvFiche.setProjet(p);
				ouvFiche.setDate(date);
				fiches.add(ouvFiche);
				/* LOCATION */
				LocationFiche locFiche = new LocationFiche();
				locFiche.setDate(date);
				locFiche.setProjet(p);
				fiches.add(locFiche);
				/* RECEPTION */
				ReceptionFiche recf = new ReceptionFiche();
				recf.setDate(date);
				recf.setProjet(p);
				fiches.add(recf);
				/* LIVRAISON */
				LivraisonFiche liv = new LivraisonFiche();
				liv.setDate(date);
				liv.setProjet(p);
				fiches.add(liv);
				/* DOCUMENT */
				DocFiche dic = new DocFiche();
				p.getDocuments().forEach(doc -> {
					DocDesignation ds = new DocDesignation();
					ds.setDocument(doc);
					ds.setIntitule(doc.getIntitule());
					ds.setDisponibilite(false);
					ds.setFiche(dic);
					dic.getDocDesignations().add(ds);
				});

				dic.setDate(date);
				dic.setProjet(p);
				fiches.add(dic);
				/* STOCK */
				StockFiche stockFiche = new StockFiche();
				stockFiche.setDate(date);
				stockFiche.setProjet(p);
				fiches.add(stockFiche);
				/* ACCIDENT */
				AccidentFiche accFiche = new AccidentFiche();
				accFiche.setDate(date);
				stockFiche.setProjet(p);
				fiches.add(accFiche);
				/* BESOIN */
				BesoinFiche bsnnFiche = new BesoinFiche();
				bsnnFiche.setDate(date);
				bsnnFiche.setProjet(p);
				fiches.add(bsnnFiche);
				/* VISITEUR */
				VisiteurFiche vstFiche = new VisiteurFiche();
				vstFiche.setDate(date);
				vstFiche.setProjet(p);
				fiches.add(vstFiche);
				entityManager.detach(p);

				p.setFichiers(fiches);
				projetRepository.save(p);
			}
		}
	}

}
