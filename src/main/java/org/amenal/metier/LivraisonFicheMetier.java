package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.amenal.dao.AccidentFicherepository;
import org.amenal.dao.ArticleRepository;
import org.amenal.dao.DestinationRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.FicheBesoinRepository;
import org.amenal.dao.LivraisonDesignationRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.dao.StockRepository;
import org.amenal.dao.VisiteurFicheRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Destination;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.Designation;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.entities.designations.LivraisonDesignation;
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
import org.amenal.rest.commande.LivraisonDesignationCommande;
import org.amenal.rest.mapper.LivraisonDesignationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LivraisonFicheMetier {
	
	@PersistenceContext
	EntityManager entityManager;


	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	DestinationRepository destinationRepository;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	LivraisonDesignationRepository livraisonDesignationRepository;

	@Autowired
	LivraisonDesignationMapper livraisonDesignationMapper;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	StockMetier stockMetier;

	@Autowired
	DocFicheRepository docFicheRepository;

	@Autowired
	AccidentFicherepository accidentFicherepository;
	
	@Autowired
	FicheBesoinRepository ficheBesoinRepository;

	@Autowired
	VisiteurFicheRepository visiteurFicheRepository;

	public void addLivraisonDesignation(LivraisonDesignationCommande livCmd) {

		LivraisonDesignation ds = livraisonDesignationMapper.toEntity(livCmd);

		Optional<Article> art = articleRepository.findById(ds.getArticleLvr().getId());
		Optional<Destination> dst = destinationRepository.findById(ds.getDestination().getId());

		if (!art.isPresent())
			throw new NotFoundException("L 'article [ " + ds.getArticleLvr().getId() + " ] est introuvable!");
		if (!dst.isPresent())
			throw new NotFoundException("La destination [ " + ds.getArticleLvr().getId() + " ] est introuvable!");

		LivraisonDesignation dss = livraisonDesignationRepository.findByArticleLvrAndDestinationAndFicheId(
				art.get(), dst.get(), ds.getFiche().getId());

		if (dss == null) {
			ds.setArticleLvr(art.get());
			ds.setCategorieLv(art.get().getCategorie().getCategorie());
			ds.setUnite(art.get().getUnite().getUnite());
			ds.setDestination(dst.get());
			livraisonDesignationRepository.save(ds);
		} else {
			dss.setQuantite(dss.getQuantite() + ds.getQuantite());
		}

	}

	public void updateLivraisonDesignation(LivraisonDesignationCommande livCmd, Integer id) {

		LivraisonDesignation ds = livraisonDesignationMapper.toEntity(livCmd);

		Optional<Article> art = articleRepository.findById(ds.getArticleLvr().getId());
		Optional<Destination> dst = destinationRepository.findById(ds.getDestination().getId());
		if (!art.isPresent())
			throw new NotFoundException("L 'article [ " + ds.getArticleLvr().getId() + " ] est introuvable!");
		if (!dst.isPresent())
			throw new NotFoundException("La destination [ " + ds.getArticleLvr().getId() + " ] est introuvable!");

		LivraisonDesignation dss = livraisonDesignationRepository.findByArticleLvrAndDestinationAndFicheId(
				art.get(), dst.get(), ds.getFiche().getId());

		if (dss == null) {
			ds.setArticleLvr(art.get());
			ds.setCategorieLv(art.get().getCategorie().getCategorie());
			ds.setUnite(art.get().getUnite().getUnite());
			ds.setDestination(dst.get());
			ds.setId(id);
			livraisonDesignationRepository.save(ds);
		} else
			dss.setQuantite(dss.getQuantite() + ds.getQuantite());

		livraisonDesignationRepository.save(ds);
	}

	public void deleteLivraisonDesignation(Integer id) {
		Optional<LivraisonDesignation> ds = livraisonDesignationRepository.findById(id);
		if (!ds.isPresent())
			throw new NotFoundException("La designation livraison [" + id + " est introuvable");
		livraisonDesignationRepository.delete(ds.get());
	}

	public void validerFicheLivraison(Integer id) {

		Optional<LivraisonFiche> fiche = livraisonFicheRepository.findById(id);

		ReceptionFiche rcF = receptionFicheRepository.findByDateAndProjet(fiche.get().getDate(),
				fiche.get().getProjet());

		if (rcF.getIsValidated()) {
			fiche.get().setIsValidated(true);
			DocFiche docF = docFicheRepository.findByDateAndProjet(fiche.get().getDate(), fiche.get().getProjet());
			AccidentFiche accF = accidentFicherepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			VisiteurFiche ficheV = visiteurFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			BesoinFiche bsnFiche = ficheBesoinRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());

			if (docF.getIsValidated() && accF.getIsValidated() && ficheV.getIsValidated() && bsnFiche.getIsValidated()) {
				List<Fiche> fiches = new ArrayList<Fiche>();
				Projet p = fiche.get().getProjet();

				LocalDate date = fiche.get().getDate().plusDays(1);
				stockMetier.validerFicheStock(fiche.get().getDate(), fiche.get().getProjet());

				System.out.println("date : " + fiche.get().getDate());

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
				dic.setDate(date);
				p.getDocuments().forEach(doc -> {
					DocDesignation ds = new DocDesignation();
					ds.setDocument(doc);
					ds.setIntitule(doc.getIntitule());
					ds.setDisponibilite(false);
					ds.setFiche(dic);
					dic.getDocDesignations().add(ds);
				});
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
				accFiche.setProjet(p);
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

		} else
			throw new BadRequestException("Vous devez tous d'abord valider la fiche de reception!");

	}

}
