package org.amenal.metier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.FournisseurRepository;

import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionAssoRepository;
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.dao.ReceptionFicheRepository;
import org.amenal.dao.pojo.FournisseurArticleBsn;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.Projet;
import org.amenal.entities.ReceptionAsso;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.ReceptionDesignationCommande;
import org.amenal.rest.mapper.ArticleMapper;
import org.amenal.rest.mapper.FournisseurArticleMapper;
import org.amenal.rest.mapper.FournisseurMapper;
import org.amenal.rest.mapper.ReceptionDesignationMapper;
import org.amenal.rest.representation.ArticlePresentation;
import org.amenal.rest.representation.CategorieReceptionDesignationPresentation;
import org.amenal.rest.representation.FournisseurArticlePresentation;
import org.amenal.rest.representation.FournisseurPresentation;
import org.amenal.rest.representation.ReceptionDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReceptionFicheMetier {

	private Boolean isAsso = false;

	@Autowired
	FournisseurRepository fournisseurRepository;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	ReceptionAssoRepository receptionAssoRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	ReceptionDesignationRepository receptionDesignationRepository;

	@Autowired
	FournisseurArticleMapper fournisseurArticleMapper;

	@Autowired
	FournisseurMapper fournisseurMapper;

	@Autowired
	ArticleMapper articleMapper;

	@Autowired
	ReceptionDesignationMapper receptionDesignationMapper;

	public List<FournisseurPresentation> ListerFournisseurNotAsso() {

		return receptionAssoRepository.findFournisseurNotAsso().stream().map(o -> fournisseurMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void addFournisseur(Integer id) {

		Optional<Fournisseur> Fournisseur = fournisseurRepository.findById(id);

		if (!Fournisseur.isPresent())
			throw new NotFoundException("le fournisseur est introuvable");

		List<ReceptionAsso> receptionAssos = receptionAssoRepository.findByFournisseur(Fournisseur.get());

		if (!receptionAssos.isEmpty())
			throw new BadRequestException("le fournisseur est deja associer");

		ReceptionAsso asso = new ReceptionAsso();
		asso.setFournisseur(Fournisseur.get());
		receptionAssoRepository.save(asso);

	}

	public void assoArticleToFournisseur(Integer idFr, Integer idArt) {

		Optional<Fournisseur> f = fournisseurRepository.findById(idFr);
		Optional<Article> art = articleRepository.findById(idArt);
		ReceptionAsso rec = new ReceptionAsso();

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + idFr + "] est inexistant");
		if (!art.isPresent())
			throw new NotFoundException("l'article [" + idArt + "] est inexistant");

		ReceptionAsso rr = receptionAssoRepository.findByFournisseurAndOthersNull(f.get());

		if (rr != null)
			receptionAssoRepository.delete(rr);

		List<ReceptionAsso> receptionAssos = receptionAssoRepository.findByFournisseurAndArticle(f.get(), art.get());

		if (receptionAssos.isEmpty()) {
			rec.setArticle(art.get());
			rec.setFournisseur(f.get());
			rec.setCategorie(art.get().getCategorie());
			receptionAssoRepository.save(rec);
		}

	}

	public List<FournisseurArticlePresentation> ListerFournisseur(Integer idProjet) {

		List<FournisseurArticleBsn> frs = new ArrayList<FournisseurArticleBsn>();
		FournisseurArticleBsn fr = new FournisseurArticleBsn();

		List<ReceptionAsso> assos = receptionAssoRepository.findByOrderByFournisseurAndCategorie();
		CategorieArticle cat = null;

		if (assos.get(0).getCategorie() != null) {
			cat = new CategorieArticle();
			cat.setId(assos.get(0).getCategorie().getId());
			cat.setCategorie(assos.get(0).getCategorie().getCategorie());
		}
		fr.setId(assos.get(0).getFournisseur().getId());
		fr.setFournisseurNom(assos.get(0).getFournisseur().getFournisseurNom());
		fr.setIsAssoWithProjet(false);
		fr.setCategories(new ArrayList<CategorieArticle>());
		if (cat != null) {
			fr.getCategories().add(cat);

		}
		frs.add(fr);

		Integer i = 0;
		Integer j = 0;
		Boolean FourIsAssoWithProjet = false;

		for (ReceptionAsso ass : assos) {

			if (ass.getArticle() != null) {
				Article ar = new Article();
				ar.setId(ass.getArticle().getId());
				ar.setCategorie(ass.getArticle().getCategorie());
				ar.setDesignation(ass.getArticle().getDesignation());
				ar.setStockable(ass.getArticle().getStockable());
				ar.setUnite(ass.getArticle().getUnite());
				ar.setIsAssoWithProjet(false);
				List<Integer> projetIds = ass.getProjets().stream().map(a -> a.getId()).collect(Collectors.toList());

				if (ass.getFournisseur().getId() == frs.get(i).getId()) {

					ass.getArticle().setIsAssoWithProjet(false);

					if (projetIds.contains(idProjet)) {

						FourIsAssoWithProjet = true;
						ar.setIsAssoWithProjet(true);

					}

					if (ar.getCategorie().getId() != frs.get(i).getCategories().get(j).getId()) {

						for (Article a : frs.get(i).getCategories().get(j).getArticles()) {

							if (a.getIsAssoWithProjet()) {
								frs.get(i).getCategories().get(j).setIsAssoWithProjet(true);
								break;
							}
						}

						CategorieArticle cat2 = new CategorieArticle();
						cat2.setId(ass.getCategorie().getId());
						cat2.setCategorie(ass.getCategorie().getCategorie());

						frs.get(i).getCategories().add(cat2);

						j++;

					}
					frs.get(i).getCategories().get(j).getArticles().add(ar);
					for (Article a : frs.get(i).getCategories().get(j).getArticles())
						if (a.getIsAssoWithProjet()) {
							frs.get(i).getCategories().get(j).setIsAssoWithProjet(true);
							break;
						}

				} else {

					frs.get(i).setIsAssoWithProjet(FourIsAssoWithProjet);
					FourIsAssoWithProjet = false;

					for (Article a : frs.get(i).getCategories().get(j).getArticles()) {

						if (a.getIsAssoWithProjet()) {
							frs.get(i).getCategories().get(j).setIsAssoWithProjet(true);
							break;

						}
					}

					if (projetIds.contains(idProjet)) {
						FourIsAssoWithProjet = true;

						ar.setIsAssoWithProjet(true);

					}

					cat = new CategorieArticle();
					fr = new FournisseurArticleBsn();

					cat.setId(ass.getCategorie().getId());
					cat.setCategorie(ass.getCategorie().getCategorie());

					fr.setId(ass.getFournisseur().getId());
					fr.setFournisseurNom(ass.getFournisseur().getFournisseurNom());
					fr.setIsAssoWithProjet(FourIsAssoWithProjet);
					fr.setCategories(new ArrayList<CategorieArticle>());

					fr.getCategories().add(cat);
					frs.add(fr);
					i++;
					j = 0;
					frs.get(i).getCategories().get(j).getArticles().add(ar);
					for (Article a : frs.get(i).getCategories().get(j).getArticles()) {

						if (a.getIsAssoWithProjet()) {
							frs.get(i).getCategories().get(j).setIsAssoWithProjet(true);
							break;

						}
					}

				}

			} else {
				if (frs.get(i).getId() != ass.getFournisseur().getId()) {

					fr = new FournisseurArticleBsn();

					fr.setId(ass.getFournisseur().getId());
					fr.setFournisseurNom(ass.getFournisseur().getFournisseurNom());
					fr.setIsAssoWithProjet(false);
					fr.setCategories(new ArrayList<CategorieArticle>());

					frs.add(fr);
					FourIsAssoWithProjet = false;
					i++;
					j = 0;
				}

			}

		}

		return frs.stream().map(o -> fournisseurArticleMapper.toRepresentation(o)).collect(Collectors.toList());
	}

	public void assoArticleFournisseurToProjet(Integer idProjet, Integer IdArticle, Integer idFour) {

		Optional<Projet> p = projetRepository.findById(idProjet);
		if (!p.isPresent())
			throw new NotFoundException("Le projet [ " + idProjet + " ] innexsistant");

		ReceptionAsso ass = receptionAssoRepository.findByFournisseurIdAndArticleId(idFour, IdArticle);

		if (ass.getProjets().contains(p.get())) {
			List<Projet> ps = ass.getProjets();
			ps.remove(p.get());
			ass.setProjets(ps);
		} else {
			List<Projet> ps = ass.getProjets();
			ps.add(p.get());
			ass.setProjets(ps);
		}

		receptionAssoRepository.save(ass);

	}

	/*********************************************************************************************/

	public void AddLigneDesignationReception(ReceptionDesignationCommande recCmd) {

		Integer ficheId = recCmd.getIdFiche();

		Optional<ReceptionFiche> fiche = receptionFicheRepository.findById(ficheId);
		Optional<Article> article = articleRepository.findById(recCmd.getIdArticle());
		Optional<Fournisseur> fr = fournisseurRepository.findById(recCmd.getIdFournisseur());

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + ficheId + " ] est introuvable !");
		if (!article.isPresent())
			throw new NotFoundException("L' article [ " + recCmd.getIdArticle() + " ] est introuvable !");
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + recCmd.getIdFournisseur() + " ] est introuvable !");

		ReceptionDesignation recDs = receptionDesignationMapper.toEntity(recCmd);

		recDs.setLibelle(article.get().getDesignation());
		recDs.setUnite(article.get().getUnite().getUnite());
		recDs.setArticle(article.get());
		recDs.setCategorie(article.get().getCategorie().getCategorie());
		recDs.setFournisseur(fr.get());
		recDs.setFournisseurNom(fr.get().getFournisseurNom());

		receptionDesignationRepository.save(recDs);

	}
	
	public void UpdateLigneDesignationReception(ReceptionDesignationCommande recCmd , Integer id) {

		Integer ficheId = recCmd.getIdFiche();

		Optional<ReceptionFiche> fiche = receptionFicheRepository.findById(ficheId);
		Optional<Article> article = articleRepository.findById(recCmd.getIdArticle());
		Optional<Fournisseur> fr = fournisseurRepository.findById(recCmd.getIdFournisseur());

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + ficheId + " ] est introuvable !");
		if (!article.isPresent())
			throw new NotFoundException("L' article [ " + recCmd.getIdArticle() + " ] est introuvable !");
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + recCmd.getIdFournisseur() + " ] est introuvable !");

		ReceptionDesignation recDs = receptionDesignationMapper.toEntity(recCmd);
		
		recDs.setId(id);
		recDs.setLibelle(article.get().getDesignation());
		recDs.setUnite(article.get().getUnite().getUnite());
		recDs.setArticle(article.get());
		recDs.setCategorie(article.get().getCategorie().getCategorie());
		recDs.setFournisseur(fr.get());
		recDs.setFournisseurNom(fr.get().getFournisseurNom());

		receptionDesignationRepository.save(recDs);

	}
	
	public void SupprimerRecDesignation(Integer Rec) {
		// TODO Auto-generated method stub
		Optional<ReceptionDesignation> ds = receptionDesignationRepository.findById(Rec);
		if (!ds.isPresent())
			throw new NotFoundException("La ligne [ " + Rec + " ] est introuvable !");

		this.receptionDesignationRepository.delete(ds.get());

	}

	public List<FournisseurArticlePresentation> ListerMaterielAssoToProjet(Integer idProjet) {

		List<FournisseurArticlePresentation> frs = new ArrayList<FournisseurArticlePresentation>();

		Optional<Projet> p = projetRepository.findById(idProjet);
		if (!p.isPresent())
			throw new NotFoundException("Le projet [ " + idProjet + " ] innexsistant");

		List<ReceptionAsso> assos = this.receptionAssoRepository.findFournisseurArticleAssoToProjet(p.get());

		for (ReceptionAsso asso : assos) {

			Fournisseur f = new Fournisseur();
			f.setId(asso.getFournisseur().getId());
			f.setFournisseurNom(asso.getFournisseur().getFournisseurNom());
			Article ar = new Article();
			ar.setId(asso.getArticle().getId());
			ar.setDesignation(asso.getArticle().getDesignation());
			ar.setUnite(asso.getArticle().getUnite());
			ar.setCategorie(asso.getArticle().getCategorie());
			f.setArticle(ar);
			frs.add(fournisseurArticleMapper.toRepresentation(f));

		}

		return frs;
	}

}
