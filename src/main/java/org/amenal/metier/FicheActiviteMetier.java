package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ActiviteDesignationRepository;
import org.amenal.dao.ActiviteFicheRepository;
import org.amenal.dao.ArticleRepository;
import org.amenal.dao.EntreeDesignationRepository;
import org.amenal.dao.EntreeRepository;
import org.amenal.dao.LotRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.QualificationOuvrierRepository;
import org.amenal.dao.SousLotDesignationRepository;
import org.amenal.dao.SousLotRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Entree;
import org.amenal.entities.Lot;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.SousLot;
import org.amenal.entities.designations.ActiviteDesignation;
import org.amenal.entities.designations.EntreeDesignation;
import org.amenal.entities.designations.SousLotDesignation;
import org.amenal.entities.designations.StockDesignation;
import org.amenal.entities.fiches.ActiviteFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.EntreeDesignationCommande;
import org.amenal.rest.representation.EntreeDesignationNonAssoPresentation;
import org.amenal.rest.representation.LotAssoPresentation;
import org.amenal.rest.representation.SousLotDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FicheActiviteMetier {

	@Autowired
	StockMetier stockMetier;
	@Autowired
	SousLotRepository sousLotRepository;
	@Autowired
	LotRepository lotRepository;
	@Autowired
	ActiviteDesignationRepository ActiviteDesignationRepository;
	@Autowired
	ActiviteFicheRepository activiteFicheRepository;
	@Autowired
	SousLotDesignationRepository sousLotDesignationRepository;
	@Autowired
	QualificationOuvrierRepository qualificationOuvrierRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	EntreeDesignationRepository entreeDesignationRepository;
	@Autowired
	ProjetRepository projetRepository;
	@Autowired
	EntreeRepository entreeRepository;

	public void DeleteDesignation(Integer id) {
		this.ActiviteDesignationRepository.deleteById(id);

	}

	public List<LotAssoPresentation> listLotParProjet(Integer projetId, Integer fid) {
		
		List<Lot> lots =projetRepository.findLotByProjet(projetId, fid);
		
		if(lots.isEmpty())
			lots = new ArrayList<Lot>();

		return lots.stream().map(o -> {
			LotAssoPresentation ll = new LotAssoPresentation();
			ll.setId(o.getId());
			ll.setLot(o.getDesignation());
			return ll;
		}).collect(Collectors.toList());
		

	}

	public void addActiviteDesignation(Integer lotId, Integer ficheId, Integer pid) {

		ActiviteDesignation ds = new ActiviteDesignation();
		List<SousLotDesignation> slotDs = new ArrayList<SousLotDesignation>();

		Optional<Lot> lot = lotRepository.findById(lotId);
		Optional<ActiviteFiche> fiche = activiteFicheRepository.findById(ficheId);

		ds.setLot(lot.get());

		for (SousLot ss : lot.get().getSousLots()) {

			List<Integer> ids = ss.getProjets().stream().map(p -> p.getId()).collect(Collectors.toList());

			if (ids.contains(pid)) {

				SousLotDesignation a = new SousLotDesignation();
				a.setDesignation(ss.getDesignation());
				a.setAvancement(0.0);
				a.setUnite(ss.getUnite().getUnite());
				a.setSousLot(ss);
				a.setActiviteDesignation(ds);
				slotDs.add(a);
			}
		}

		ds.setFiche(fiche.get());
		ds.setSousLotDesignations(slotDs);

		fiche.get().getActiviteDesignations().add(ds);

	}
	
	public void deleteEntreeDesignation(Integer id) {
		entreeDesignationRepository.deleteById(id);
	}
	
	public void updateSousLotDesignation(Integer id , Double p) {
		
		Optional<SousLotDesignation> slotDs = sousLotDesignationRepository.findById(id);
		
		slotDs.get().setAvancement(p);
		
	}

	public void UpdateEntreeDesignation(EntreeDesignationCommande entreeDsCmd, Integer id) {
		EntreeDesignation entree = entreeDesignationRepository.findById(id).orElse(null);

		if (entreeDsCmd.getType() != null) {
			if (entreeDsCmd.getType().equals(Entree.OUVRIER)) {
				Optional<QualificationOuvrier> qual = qualificationOuvrierRepository
						.findById(entreeDsCmd.getEntreeId());
				entree.setQualification(qual.get());
				entree.setQuantite(entreeDsCmd.getQuantite());
				entree.setType(Entree.OUVRIER);
			} else if (entreeDsCmd.getType().equals(Entree.ARTICLE)) {
				Optional<Article> art = articleRepository.findById(entreeDsCmd.getEntreeId());
				entree.setArticle(art.get());
				entree.setQuantite(entreeDsCmd.getQuantite());
				entree.setType(Entree.ARTICLE);
			}
		}

	}

	public void addEntreeToSousLotDesignation(Integer sLotDsId, EntreeDesignationCommande entreeDsCmd) {

		Optional<SousLotDesignation> slotDs = sousLotDesignationRepository.findById(sLotDsId);
		EntreeDesignation entree = new EntreeDesignation();

		if (!slotDs.isPresent())
			throw new BadRequestException("Sous lot introuvable");

		if (entreeDsCmd.getType() != null) {
			if (entreeDsCmd.getType().equals(Entree.OUVRIER)) {
				Optional<QualificationOuvrier> qual = qualificationOuvrierRepository
						.findById(entreeDsCmd.getEntreeId());
				entree.setQualification(qual.get());
				entree.setQuantite(entreeDsCmd.getQuantite());
				entree.setType(Entree.OUVRIER);
				slotDs.get().getEntreeDesignations().add(entree);
			} else if (entreeDsCmd.getType().equals(Entree.ARTICLE)) {
				Optional<Article> art = articleRepository.findById(entreeDsCmd.getEntreeId());
				entree.setArticle(art.get());
				entree.setQuantite(entreeDsCmd.getQuantite());
				entree.setType(Entree.ARTICLE);
				slotDs.get().getEntreeDesignations().add(entree);
			}
		}

		entree.setSousLotDesignation(slotDs.get());
		entreeDesignationRepository.save(entree);

	}

	public List<SousLotDesignationPresentation> listEntreeDesignationNoAsso(Integer projetId, LocalDate date) {

		List<SousLotDesignationPresentation> slotDs = new ArrayList<SousLotDesignationPresentation>();

		List<SousLot> slots = sousLotRepository.findAll();

		List<StockDesignation> stockDss = this.stockMetier.getStockLigneDesignation(projetId, date).stream()
				.map(o -> o.getStockDesignations()).flatMap(Collection::stream).collect(Collectors.toList());

		for (SousLot slot : slots) {
			List<EntreeDesignationNonAssoPresentation> entreeDs$ = stockDss.stream()
					.map(o -> fromStockDesignationToEntreeDesignation(o)).collect(Collectors.toList());

			SousLotDesignationPresentation ss = new SousLotDesignationPresentation();
			List<EntreeDesignationNonAssoPresentation> entreeDs = new ArrayList<EntreeDesignationNonAssoPresentation>();

			ss.setId(slot.getId());

			entreeDs = slot.getEntrees().stream().map(o -> fromEntreeToEntreeDesignation(o))
					.collect(Collectors.toList());
			if (entreeDs == null)
				entreeDs = new ArrayList<EntreeDesignationNonAssoPresentation>();

			List<EntreeDesignationNonAssoPresentation> entreeDs$ToDelete = new ArrayList<EntreeDesignationNonAssoPresentation>();

			for (EntreeDesignationNonAssoPresentation e : entreeDs$) {
				for (EntreeDesignationNonAssoPresentation ee : entreeDs) {
					if (e.getId() == ee.getId() && e.getType().equals(ee.getType())) {
						ee.setQuantite(e.getQuantite());
						entreeDs$ToDelete.add(e);
					}
				}
			}

			entreeDs$.removeAll(entreeDs$ToDelete);

			entreeDs.addAll(entreeDs$);
			ss.setEntreeDesignationNonAssoPresentations(entreeDs);

			slotDs.add(ss);

		}

		return slotDs;

	}

	/************************************************************
	 * PRIVATE_METHOD
	 ************************************************************/
	private EntreeDesignationNonAssoPresentation fromEntreeToEntreeDesignation(Entree e) {

		EntreeDesignationNonAssoPresentation ed = new EntreeDesignationNonAssoPresentation();
		if (e.getType().equals(Entree.OUVRIER)) {
			ed.setEntreeNom(e.getQualification().getCode());
			ed.setUnite("H");
			ed.setId(e.getQualification().getId());
			ed.setIsRecomander(true);
			ed.setType(Entree.OUVRIER);

		} else if (e.getType().equals(Entree.ARTICLE)) {
			ed.setEntreeNom(e.getArticle().getDesignation());
			ed.setUnite(e.getArticle().getUnite().getUnite());
			ed.setId(e.getArticle().getId());
			ed.setIsRecomander(true);
			ed.setType(Entree.ARTICLE);
		}

		return ed;
	}

	private EntreeDesignationNonAssoPresentation fromStockDesignationToEntreeDesignation(StockDesignation e) {

		EntreeDesignationNonAssoPresentation ed = new EntreeDesignationNonAssoPresentation();
		if (e.getEntreeType().equals(Entree.OUVRIER)) {
			ed.setEntreeNom(e.getQualifOuvrier().getCode());
			ed.setUnite("H");
			ed.setQuantite(e.getQuantite());
			ed.setId(e.getQualifOuvrier().getId());
			ed.setIsRecomander(false);
			ed.setType(Entree.OUVRIER);

		} else if (e.getEntreeType().equals(Entree.ARTICLE)) {
			ed.setEntreeNom(e.getArticle().getDesignation());
			ed.setUnite(e.getArticle().getUnite().getUnite());
			ed.setId(e.getArticle().getId());
			ed.setQuantite(e.getQuantite());
			ed.setIsRecomander(false);
			ed.setType(Entree.ARTICLE);
		}

		return ed;

	}

}
