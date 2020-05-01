package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.amenal.dao.AccidentFicherepository;
import org.amenal.dao.ArticleRepository;
import org.amenal.dao.BesoinDesignationRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.FicheBesoinRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.QualificationOuvrierRepository;
import org.amenal.dao.VisiteurFicheRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Besion;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.designations.BesoinDesignation;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.amenal.rest.commande.BesoinDesignationCommande;
import org.amenal.rest.representation.BesoinRepresentation;
import org.amenal.rest.mapper.BesoinDesignationMapper;

@Service
@Transactional
public class BesoinFicheMetier {

	@Autowired
	FicheBesoinRepository ficheBesoinRepository;
	@Autowired
	BesoinDesignationRepository besionDesignationRepository;
	@Autowired
	ArticleRepository articleRepository;
	@Autowired
	QualificationOuvrierRepository qualificationOuvrierRepository;
	@Autowired
	BesoinDesignationMapper BesoinDesignationMapper;
	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;
	@Autowired
	AccidentFicherepository accidentFicherepository;
	@Autowired
	DocFicheRepository docFicheRepository;
	@Autowired
	VisiteurFicheRepository visiteurFicheRepository;
	@Autowired
	StockMetier stockMetier;
	@Autowired
	ProjetRepository projetRepository;
	@PersistenceContext
	EntityManager entityManager;


	public List<BesoinRepresentation> listeBesion() {
		
		

		List<BesoinRepresentation> listBesion = new ArrayList<BesoinRepresentation>();

		for (QualificationOuvrier qual : qualificationOuvrierRepository.findAll()) {
			BesoinRepresentation b = new BesoinRepresentation();
			b.setType("ouv");
			b.setId(qual.getId());
			b.setBesion(qual.getCode());
			b.setUnite("H");
			listBesion.add(b);
		}

		for (Article art : articleRepository.findAll()) {
			BesoinRepresentation b = new BesoinRepresentation();
			b.setType("art");
			b.setId(art.getId());
			b.setBesion(art.getDesignation());
			b.setUnite(art.getUnite().getUnite());

			listBesion.add(b);
		}

		return listBesion;

	}

	public void addLigneDesignationBesion(BesoinDesignationCommande bsrCmd) {

		BesoinDesignation bs = BesoinDesignationMapper.toEntity(bsrCmd);
		if (bsrCmd.getBesoinType() != null)
			if (bsrCmd.getBesoinType().equals("ouv")) {

				Optional<QualificationOuvrier> qual = qualificationOuvrierRepository.findById(bsrCmd.getBesoinId());
				bs.setQualification(qual.get());
				bs.setDesignation(qual.get().getCode());

			} else if (bsrCmd.getBesoinType().equals("art")) {

				Optional<Article> art = articleRepository.findById(bsrCmd.getBesoinId());
				bs.setArticle(art.get());
				bs.setDesignation(art.get().getDesignation());
			}

		Optional<BesoinFiche> fiche = ficheBesoinRepository.findById(bs.getFiche().getId());

		if (!fiche.isPresent())
			throw new NotFoundException("Fiche introuvable!");

		bs.setFiche(fiche.get());

		besionDesignationRepository.save(bs);

	}

	public void updateLigneDesignationBesion(BesoinDesignationCommande bsrCmd, Integer id) {

		BesoinDesignation bs = BesoinDesignationMapper.toEntity(bsrCmd);

		if (bsrCmd.getBesoinType() != null) {

			if (bsrCmd.getBesoinType().equals("ouv")) {

				Optional<QualificationOuvrier> qual = qualificationOuvrierRepository.findById(bsrCmd.getBesoinId());
				bs.setQualification(qual.get());
				bs.setDesignation(qual.get().getCode());

			}
			if (bsrCmd.getBesoinType().equals("art")) {

				Optional<Article> art = articleRepository.findById(bsrCmd.getBesoinId());
				bs.setArticle(art.get());
				bs.setDesignation(art.get().getDesignation());
			}
		}

		Optional<BesoinFiche> fiche = ficheBesoinRepository.findById(bs.getFiche().getId());

		if (!fiche.isPresent())
			throw new NotFoundException("Fiche introuvable!");

		bs.setFiche(fiche.get());
		bs.setId(id);

		besionDesignationRepository.save(bs);

	}

	public void DeleteLigneDesignationBesion(Integer id) {

		Optional<BesoinDesignation> bs = besionDesignationRepository.findById(id);

		if (!bs.isPresent())
			throw new NotFoundException("designation introuvable!");

		besionDesignationRepository.delete(bs.get());

	}

	public void validerFicheBesion(Integer id) {

		Optional<BesoinFiche> fiche = ficheBesoinRepository.findById(id);

		if (!fiche.get().getIsValidated()) {

			Projet p = fiche.get().getProjet();

			for (BesoinDesignation d : fiche.get().getBesoinDesignations()) {
				if (!d.getSatisfaction())
					throw new BadRequestException("Vous devez satisfaire tous les besoins avant de valider la fiche!");
			}

			LivraisonFiche livF = livraisonFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			AccidentFiche accF = accidentFicherepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());
			DocFiche ficheDoc = docFicheRepository.findByDateAndProjet(fiche.get().getDate(), fiche.get().getProjet());
			VisiteurFiche ficheV = visiteurFicheRepository.findByDateAndProjet(fiche.get().getDate(),
					fiche.get().getProjet());

			fiche.get().setIsValidated(true);

			if (livF.getIsValidated() && accF.getIsValidated() && ficheDoc.getIsValidated()
					&& ficheV.getIsValidated()) {

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
				BesoinFiche bsnFiche = new BesoinFiche();
				bsnFiche.setDate(date);
				bsnFiche.setProjet(p);
				fiches.add(bsnFiche);
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
