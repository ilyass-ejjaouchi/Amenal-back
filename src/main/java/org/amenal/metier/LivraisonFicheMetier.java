package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.DestinationRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.LivraisonDesignationRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.dao.StockRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Destination;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.Designation;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.entities.fiches.StockFiche;
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

	public void addLivraisonDesignation(LivraisonDesignationCommande livCmd) {

		LivraisonDesignation ds = livraisonDesignationMapper.toEntity(livCmd);

		Optional<Article> art = articleRepository.findById(ds.getArticleLvr().getId());
		Optional<Destination> dst = destinationRepository.findById(ds.getDestination().getId());
		if (!art.isPresent())
			throw new NotFoundException("L 'article [ " + ds.getArticleLvr().getId() + " ] est introuvable!");
		if (!dst.isPresent())
			throw new NotFoundException("La destination [ " + ds.getArticleLvr().getId() + " ] est introuvable!");

		ds.setArticleLvr(art.get());
		ds.setCategorieLv(art.get().getCategorie().getCategorie());
		ds.setUnite(art.get().getUnite().getUnite());
		ds.setDestination(dst.get());

		livraisonDesignationRepository.save(ds);
	}

	public void updateLivraisonDesignation(LivraisonDesignationCommande livCmd, Integer id) {

		LivraisonDesignation ds = livraisonDesignationMapper.toEntity(livCmd);

		Optional<Article> art = articleRepository.findById(ds.getArticleLvr().getId());
		Optional<Destination> dst = destinationRepository.findById(ds.getDestination().getId());
		if (!art.isPresent())
			throw new NotFoundException("L 'article [ " + ds.getArticleLvr().getId() + " ] est introuvable!");
		if (!dst.isPresent())
			throw new NotFoundException("La destination [ " + ds.getArticleLvr().getId() + " ] est introuvable!");

		ds.setArticleLvr(art.get());
		ds.setCategorieLv(art.get().getCategorie().getCategorie());
		ds.setDestination(dst.get());
		ds.setId(id);

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

		ReceptionFiche rcF = receptionFicheRepository.findByDate(fiche.get().getDate());

		if (rcF.getIsValidated()) {
			fiche.get().setIsValidated(true);
			DocFiche docF = docFicheRepository.findByDate(fiche.get().getDate());

			if (docF.getIsValidated()) {
				List<Fiche> fiches = new ArrayList<Fiche>();
				Projet p = fiche.get().getProjet();
				if (fiche.get().getDate().isBefore(LocalDate.now())) {
					
					LocalDate date = fiche.get().getDate().plusDays(1);
					stockMetier.validerFicheStock(fiche.get().getDate());

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
					p.getDocuments().forEach(doc->{
						DocDesignation ds = new DocDesignation();
						ds.setDocument(doc);
						ds.setIntitule(doc.getIntitule());
						ds.setDisponibilite(false);
						ds.setDocFiche(dic);
						dic.getDocDesignations().add(ds);
					});
					dic.setProjet(p);
					fiches.add(dic);
					/* STOCK */
					StockFiche stockFiche = new StockFiche();
					stockFiche.setDate(date);
					stockFiche.setProjet(p);
					fiches.add(stockFiche);
					
					p.getFichiers().addAll(fiches);
					projetRepository.save(p);
				}
			}

		} else
			throw new BadRequestException("Vous devez tous d'abord valider la fiche de reception!");

	}

}
