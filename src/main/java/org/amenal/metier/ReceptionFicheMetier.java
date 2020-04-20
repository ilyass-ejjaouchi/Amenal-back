package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.CategorieArticleRepository;
import org.amenal.dao.DocFicheRepository;
import org.amenal.dao.FicheRepository;
import org.amenal.dao.FournisseurRepository;
import org.amenal.dao.LivraisonDesignationRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.LocationFicheRepository;
import org.amenal.dao.OuvrierFicheRepository;
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
import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.entities.fiches.Fiche;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.FournisseurCommande;
import org.amenal.rest.commande.ReceptionDesignationCommande;
import org.amenal.rest.mapper.ArticleMapper;
import org.amenal.rest.mapper.FournisseurArticleMapper;
import org.amenal.rest.mapper.FournisseurMapper;
import org.amenal.rest.mapper.ReceptionDesignationMapper;
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
	OuvrierFicheRepository ouvrierFicheRepository;

	@Autowired
	LocationFicheRepository locationFicheRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	ReceptionFicheRepository receptionFicheRepository;

	@Autowired
	ReceptionDesignationRepository receptionDesignationRepository;

	@Autowired
	FournisseurArticleMapper fournisseurArticleMapper;

	@Autowired
	CategorieArticleRepository categorieArticleRepository;

	@Autowired
	DocFicheRepository docFicheRepository;

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	FournisseurMapper fournisseurMapper;

	@Autowired
	ArticleMapper articleMapper;

	@Autowired
	LivraisonDesignationRepository livraisonDesignationRepository;

	@Autowired
	ReceptionDesignationMapper receptionDesignationMapper;

	public List<FournisseurPresentation> ListerFournisseurNotAsso() {

		return receptionAssoRepository.findFournisseurNotAsso().stream().map(o -> fournisseurMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void addFournisseur(Integer id, FournisseurCommande frCmd) {

		if (id == -1) {
			Fournisseur fr = fournisseurMapper.toEntity(frCmd);

			Fournisseur fr$ = fournisseurRepository.findByFournisseurNom(fr.getFournisseurNom());

			if (fr$ != null)
				throw new BadRequestException("le fournisseur  [" + fr.getFournisseurNom() + "] est deja ajouté");

			id = fournisseurRepository.save(fr).getId();

		}

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

	public void DeAssoCategorieWithFournisseur(Integer idFr, Integer idCat) {

		Optional<Fournisseur> Fournisseur = fournisseurRepository.findById(idFr);

		if (!Fournisseur.isPresent())
			throw new NotFoundException("le fournisseur est introuvable");

		Optional<CategorieArticle> cat = categorieArticleRepository.findById(idCat);

		List<ReceptionDesignation> ds = receptionDesignationRepository
				.findFournisseurArticleCategorieAssoToFicheReception(Fournisseur.get(), cat.get());

		if (!ds.isEmpty()) {
			throw new BadRequestException("le couple categorie/fournisseur  [" + Fournisseur.get().getFournisseurNom()
					+ " / " + cat.get().getCategorie() + "] ne peut pas etre supprimé , "
					+ "il est associer a une fiche non valide");
		}

		List<ReceptionAsso> reces = receptionAssoRepository.findByFournisseurAndCategorie(Fournisseur.get(), cat.get());
		List<String> projets = new ArrayList<String>();
		reces.forEach(r -> {
			projets.addAll(r.getProjets().stream().map(o -> o.getAbreveation()).collect(Collectors.toList()));
		});

		if (!projets.isEmpty()) {
			String ps = "";
			for (String p : projets)
				ps = ps + " " + p;
			throw new BadRequestException("le couple categorie/fournisseur  [" + Fournisseur.get().getFournisseurNom()
					+ " / " + cat.get().getCategorie() + "] ne peut pas etre supprimé , "
					+ "il est associer aux projet suivant : " + ps);
		} else {
			reces.forEach(r -> {
				receptionAssoRepository.delete(r);
			});

		}

	}

	public void DeAssoArticleWithFournisseur(Integer idFr, Integer idArt) {

		Optional<Fournisseur> Fournisseur = fournisseurRepository.findById(idFr);

		if (!Fournisseur.isPresent())
			throw new NotFoundException("le fournisseur est introuvable");

		Optional<Article> article = articleRepository.findById(idArt);

		if (!article.isPresent())
			throw new NotFoundException("l' article est introuvable");

		List<ReceptionDesignation> ds = receptionDesignationRepository
				.findFournisseurArticleAssoToFicheReception(Fournisseur.get(), article.get());

		if (!ds.isEmpty()) {
			throw new BadRequestException("le couple article/fournisseur  [" + Fournisseur.get().getFournisseurNom()
					+ " / " + article.get().getDesignation() + "] ne peut pas etre supprimé , "
					+ "il est associer a une fiche non valide");
		}

		ReceptionAsso reces = receptionAssoRepository.findByFournisseurAndArticle(Fournisseur.get(), article.get());

		List<String> projets = reces.getProjets().stream().map(r -> r.getAbreveation()).collect(Collectors.toList());

		if (!projets.isEmpty()) {
			String ps = "";
			for (String p : projets)
				ps = ps + " " + p;
			throw new BadRequestException("le couple article/fournisseur  [" + Fournisseur.get().getFournisseurNom()
					+ " / " + article.get().getDesignation() + "] ne peut pas etre supprimé , "
					+ "il est associer aux projet suivant : " + ps);
		} else {
			receptionAssoRepository.delete(reces);

		}

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

		ReceptionAsso receptionAssos = receptionAssoRepository.findByFournisseurAndArticle(f.get(), art.get());

		if (receptionAssos == null) {
			rec.setArticle(art.get());
			rec.setFournisseur(f.get());
			receptionAssoRepository.save(rec);
		}

	}

	public List<FournisseurArticlePresentation> ListerFournisseur(Integer idProjet) {

		List<FournisseurArticleBsn> frs = new ArrayList<FournisseurArticleBsn>();
		FournisseurArticleBsn fr = new FournisseurArticleBsn();

		List<ReceptionAsso> assos = receptionAssoRepository.findByOrderByFournisseurAndCategorie();
		CategorieArticle cat = null;
		if (!assos.isEmpty()) {
			if (assos.get(0).getArticle() != null) {
				cat = new CategorieArticle();
				cat.setId(assos.get(0).getArticle().getCategorie().getId());
				cat.setCategorie(assos.get(0).getArticle().getCategorie().getCategorie());
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
					List<Integer> projetIds = ass.getProjets().stream().map(a -> a.getId())
							.collect(Collectors.toList());

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
							cat2.setId(ass.getArticle().getCategorie().getId());
							cat2.setCategorie(ass.getArticle().getCategorie().getCategorie());

							frs.get(i).getCategories().add(cat2);

							j++;

						}
						frs.get(i).getCategories().get(j).getArticles().add(ar);
						for (Article a : frs.get(i).getCategories().get(j).getArticles())
							if (a.getIsAssoWithProjet()) {
								frs.get(i).getCategories().get(j).setIsAssoWithProjet(true);
								break;
							}

						frs.get(i).setIsAssoWithProjet(FourIsAssoWithProjet);

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

						cat.setId(ass.getArticle().getCategorie().getId());
						cat.setCategorie(ass.getArticle().getCategorie().getCategorie());

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
		}

		return frs.stream().map(o -> fournisseurArticleMapper.toRepresentation(o)).collect(Collectors.toList());
	}

	public void assoArticleFournisseurToProjet(Integer idProjet, Integer IdArticle, Integer idFour) {

		Optional<Projet> p = projetRepository.findById(idProjet);
		if (!p.isPresent())
			throw new NotFoundException("Le projet [ " + idProjet + " ] innexsistant");

		Optional<Fournisseur> fr = fournisseurRepository.findById(idFour);
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + idFour + " ] est introuvable !");
		Optional<Article> art = articleRepository.findById(IdArticle);

		ReceptionAsso ass = receptionAssoRepository.findByFournisseurIdAndArticleId(fr.get(), art.get(), p.get());
		if (ass == null) {
			throw new NotFoundException(
					"La article [ " + art.get().getDesignation() + " ] fournit par [ " + fr.get().getFournisseurNom()
							+ " ] " + "est associer a une fiche non valide du projet " + p.get().getAbreveation());

		} else if (ass.getProjets().contains(p.get())) {
			List<Projet> ps = ass.getProjets();
			ps.remove(p.get());
			ass.setProjets(ps);
		} else {
			List<Projet> ps = ass.getProjets();
			ps.add(p.get());
			ass.setProjets(ps);
		}

	}

	public void assoFournisseurToProjet(Integer idProjet, Integer idFour) {

		Optional<Projet> p = projetRepository.findById(idProjet);
		if (!p.isPresent())
			throw new NotFoundException("Le projet [ " + idProjet + " ] innexsistant");

		Optional<Fournisseur> fr = fournisseurRepository.findById(idFour);
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + idFour + " ] est introuvable !");

		List<ReceptionAsso> assos = receptionAssoRepository.findByFournisseurAndArticleNotAssoToFiche(fr.get(),
				p.get());

		List<Projet> projets = new ArrayList<Projet>();
		projets.addAll(assos.stream().map(ass -> ass.getProjets()).flatMap(List::stream).collect(Collectors.toList()));

		if (projets.contains(p.get()))
			for (ReceptionAsso ass : assos)
				ass.getProjets().remove(p.get());
		else
			for (ReceptionAsso ass : assos)
				ass.getProjets().add(p.get());

	}

	public void assoCategorieFournisseurToProjet(Integer idProjet, Integer idFour, Integer idCat) {

		Optional<Projet> p = projetRepository.findById(idProjet);
		if (!p.isPresent())
			throw new NotFoundException("Le projet [ " + idProjet + " ] innexsistant");

		Optional<CategorieArticle> c = categorieArticleRepository.findById(idCat);
		if (!c.isPresent())
			throw new NotFoundException("La categorie [ " + idCat + " ] est introuvable !");

		Optional<Fournisseur> fr = fournisseurRepository.findById(idFour);
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + idFour + " ] est introuvable !");

		List<ReceptionAsso> assos = receptionAssoRepository.findByCategorieAndArticleNotAssoToFiche(fr.get(), c.get(),
				p.get());

		List<Projet> projets = new ArrayList<Projet>();
		projets.addAll(assos.stream().map(ass -> ass.getProjets()).flatMap(List::stream).collect(Collectors.toList()));

		if (projets.contains(p.get()))
			for (ReceptionAsso ass : assos)
				ass.getProjets().remove(p.get());
		else
			for (ReceptionAsso ass : assos)
				ass.getProjets().add(p.get());

	}

	/*********************************************************************************************/

	public void AddLigneDesignationReception(ReceptionDesignationCommande recCmd) {

		Integer ficheId = recCmd.getIdFiche();

		Optional<ReceptionFiche> fiche = receptionFicheRepository.findById(ficheId);
		Optional<Article> article = articleRepository.findById(recCmd.getIdArticle());
		Optional<Fournisseur> fr = fournisseurRepository.findById(recCmd.getIdFournisseur());

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + ficheId + " ] est introuvable !");
		else if (fiche.get().getIsValidated())
			throw new BadRequestException("La fiche est deja valider!");
		if (!article.isPresent())
			throw new NotFoundException("L' article [ " + recCmd.getIdArticle() + " ] est introuvable !");
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + recCmd.getIdFournisseur() + " ] est introuvable !");

		ReceptionDesignation recDs = receptionDesignationMapper.toEntity(recCmd);

		recDs.setLibelle(article.get().getDesignation());
		recDs.setUnitee(article.get().getUnite().getUnite());
		recDs.setArticle(article.get());
		recDs.setCategorie(article.get().getCategorie().getCategorie());
		recDs.setRecFournisseur(fr.get());
		recDs.setFournisseurNom(fr.get().getFournisseurNom());

		receptionDesignationRepository.save(recDs);

	}

	public void UpdateLigneDesignationReception(ReceptionDesignationCommande recCmd, Integer id) {

		Integer ficheId = recCmd.getIdFiche();

		Optional<ReceptionFiche> fiche = receptionFicheRepository.findById(ficheId);
		Optional<Article> article = articleRepository.findById(recCmd.getIdArticle());
		Optional<Fournisseur> fr = fournisseurRepository.findById(recCmd.getIdFournisseur());

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + ficheId + " ] est introuvable !");
		else if (fiche.get().getIsValidated())
			throw new BadRequestException("La fiche est deja valider!");
		if (!article.isPresent())
			throw new NotFoundException("L' article [ " + recCmd.getIdArticle() + " ] est introuvable !");
		if (!fr.isPresent())
			throw new NotFoundException("Le fournisseur [ " + recCmd.getIdFournisseur() + " ] est introuvable !");

		ReceptionDesignation recDs = receptionDesignationMapper.toEntity(recCmd);

		List<LivraisonDesignation> ll = livraisonDesignationRepository.findDesignationByArticleAndProjet(article.get(),
				fiche.get().getProjet());

		Double qt = 0.0;
		for (LivraisonDesignation l : ll) {
			qt += l.getQuantite();
		}

		if (recDs.getQuantite() < qt)
			throw new BadRequestException(
					"Vous avez deja livrais [ " + qt + " de l' article" + article.get().getDesignation()
							+ " ],la quantité ne dois pas etre inferieur a la quantié livrais!");

		recDs.setId(id);
		recDs.setLibelle(article.get().getDesignation());
		recDs.setUnitee(article.get().getUnite().getUnite());
		recDs.setArticle(article.get());
		recDs.setCategorie(article.get().getCategorie().getCategorie());
		recDs.setRecFournisseur(fr.get());
		recDs.setFournisseurNom(fr.get().getFournisseurNom());

		receptionDesignationRepository.save(recDs);

	}

	public void SupprimerRecDesignation(Integer Rec) {
		// TODO Auto-generated method stub
		Optional<ReceptionDesignation> ds = receptionDesignationRepository.findById(Rec);
		if (!ds.isPresent())
			throw new NotFoundException("La ligne [ " + Rec + " ] est introuvable !");
		else if (ds.get().getReceptionfiche().getIsValidated())
			throw new BadRequestException("La fiche est deja valider!");

		List<LivraisonDesignation> ll = livraisonDesignationRepository
				.findDesignationByArticleAndProjet(ds.get().getArticle(), ds.get().getReceptionfiche().getProjet());

		if (!ll.isEmpty())
			throw new BadRequestException(
					"Vous ne pouvais pas supprimer cette reception , l' article associer est livrais!");

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

	public void validerFicheReception(Integer idFiche) {
		Optional<ReceptionFiche> fiche = receptionFicheRepository.findById(idFiche);

		if (!fiche.isPresent())
			throw new NotFoundException("La fiche [ " + idFiche + " ] est introuvable !");

		OuvrierFiche ficheOuv = ouvrierFicheRepository.findByDateAndProjet(fiche.get().getDate(),
				fiche.get().getProjet());

		if (!ficheOuv.getIsValidated())
			throw new BadRequestException("voudevez tous d abord valider l' afiche ouvrier!");

		LocationFiche ficheLoc = locationFicheRepository.findByDateAndProjet(fiche.get().getDate(),
				fiche.get().getProjet());

		if (!ficheLoc.getIsValidated())
			throw new BadRequestException("voudevez tous d abord valider l' afiche location!");

		for (ReceptionDesignation ds : fiche.get().getReceptionDesignations()) {

			System.out.println(ds.getObservation());
			System.out.println(ds.getObservation());
			System.out.println(ds.getObservation());


			if (ds.getObservation() == null)
				ds.setObservation("Rien a signaler");
			if (ds.getObservation().trim() == "")
				ds.setObservation("Rien a signaler");
			receptionDesignationRepository.save(ds);
		}

		fiche.get().setIsValidated(true);

	}

}
