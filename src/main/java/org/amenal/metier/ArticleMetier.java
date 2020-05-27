package org.amenal.metier;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.CategorieArticleRepository;
import org.amenal.dao.LivraisonDesignationRepository;
import org.amenal.dao.ReceptionAssoRepository;
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.ReceptionAsso;
import org.amenal.entities.Unite;
import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.ArticleCommande;
import org.amenal.rest.commande.CategorieArticleCommande;
import org.amenal.rest.mapper.ArticleMapper;
import org.amenal.rest.mapper.CategorieArticleMapper;
import org.amenal.rest.representation.CategorieArticlePresentation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ArticleMetier {

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	CategorieArticleRepository categorieArticleRepository;

	@Autowired
	UniteRepository uniteRepository;

	@Autowired
	ReceptionDesignationRepository receptionDesignationRepository;

	@Autowired
	ReceptionAssoRepository receptionAssoRepository;

	@Autowired
	LivraisonDesignationRepository livraisonDesignationRepository;

	@Autowired
	ArticleMapper articleMapper;

	@Autowired
	CategorieArticleMapper categorieArticleMapper;

	@SuppressWarnings("deprecation")
	public void emportExcelFile(MultipartFile excelFile) throws EncryptedDocumentException, InvalidFormatException {

		Workbook workbook;
		try {
			new WorkbookFactory();
			workbook = WorkbookFactory.create(excelFile.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new BadRequestException("format de fichier non supporter!");
		}
		Sheet dataSheet = workbook.getSheetAt(0);

		int i = 1;
		for (Row row : dataSheet) {
			if (i > 1
					&& articleRepository.findByDesignation(row.getCell(0).getStringCellValue().toUpperCase()) == null) {
				Article article = new Article();

				if (row.getCell(0).getCellTypeEnum() == CellType.STRING)
					article.setDesignation(row.getCell(0).getStringCellValue());
				else
					throw new BadRequestException(
							"la colonne DESIGNATION dois etre en format chacractaire (" + i + ",1)");
				if (row.getCell(1).getCellTypeEnum() == CellType.STRING) {

					String unt = row.getCell(1).getStringCellValue().toUpperCase();

					Unite unt$ = uniteRepository.findByUnite(unt);

					if (unt$ == null) {
						unt$ = new Unite();
						unt$.setUnite(unt);
					}

					article.setUnite(unt$);

				} else
					throw new BadRequestException("la colonne UNITE dois etre en format chacractaire (" + i + ",2)");
				if (row.getCell(2).getCellTypeEnum() == CellType.STRING) {

					String cat = row.getCell(2).getStringCellValue().toUpperCase();

					CategorieArticle cat$ = categorieArticleRepository.findByCategorie(cat);
					if (cat$ == null) {
						cat$ = new CategorieArticle();
						cat$.setCategorie(cat);
					}

					cat$.setShowCat(true);

					article.setCategorie(cat$);
				} else
					throw new BadRequestException(
							"la colonne CATEGORIE dois etre en format chacractaire (" + i + ",3)");
				if (row.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {

					Integer stockable = (int) row.getCell(3).getNumericCellValue();
					if (stockable == 1)
						article.setStockable(true);
					else if (stockable == 0)
						article.setStockable(false);
					else
						throw new BadRequestException("la colonne STOCKABLE dois etre entre 0 ou 1 (" + i + ",4)");

				} else
					throw new BadRequestException("la colonne STOCKABLE dois etre entre 0 ou 1 (" + i + ",4)");

				article.setShowArt(true);
				articleRepository.save(article);

			}
			i++;

		}

	}

	public Integer addCategorieArticle(CategorieArticleCommande CatCmd) {

		CategorieArticle cat = categorieArticleMapper.toEntity(CatCmd);

		CategorieArticle cc = categorieArticleRepository.findByCategorie(cat.getCategorie());

		if (cc != null)
			if (cc.getShowCat())
				throw new NotFoundException("l' acategorie  [ " + cat.getCategorie() + " ] est deja existante!");
			else {
				cc.setShowCat(true);
				return cc.getId();
			}

		else {
			cat.setShowCat(true);
			return categorieArticleRepository.save(cat).getId();
		}
	}

	public void editCategorieArticle(CategorieArticleCommande CatCmd, Integer id) {
		CategorieArticle cc = categorieArticleMapper.toEntity(CatCmd);

		CategorieArticle ca = categorieArticleRepository.findByCategorie(cc.getCategorie());

		if (ca != null && ca.getId() != id)
			if (ca.getShowCat())
				throw new BadRequestException("La categorie est deja ajouter!");
			else {
				Optional<CategorieArticle> cat = categorieArticleRepository.findById(id);

				cat.get().getArticles().forEach(a -> {
					a.setCategorie(ca);
					articleRepository.save(a);
				});
				ca.setShowCat(true);
				cat.get().setArticles(null);
				categorieArticleRepository.delete(cat.get());

			}
		else {

			Optional<CategorieArticle> cat = categorieArticleRepository.findById(id);

			if (!cat.isPresent())
				throw new NotFoundException("La categorie dont l' id [ " + id + " ] est inexistante!");
			cat.get().setCategorie(cc.getCategorie());

			List<LivraisonDesignation> livs = livraisonDesignationRepository
					.findDesignationByCategorieArticleAndFicheNotValid(id);

			if (!livs.isEmpty()) {
				livs.forEach(l -> {
					l.setCategorieLv(cat.get().getCategorie());
					livraisonDesignationRepository.save(l);
				});
			}

			List<ReceptionDesignation> dss = receptionDesignationRepository
					.findDesignationByCategorieAndFicheNotValid(id);

			if (!dss.isEmpty()) {
				dss.forEach(l -> {
					l.setCategorie(cat.get().getCategorie());
				});
			}
		}

	}

	public Integer EditArticle(ArticleCommande articleCmd, Integer id) {

		Optional<CategorieArticle> cat = categorieArticleRepository.findById(articleCmd.getCategorieID());
		Unite unite = uniteRepository.findByUnite(articleCmd.getUnite());
		if (!cat.isPresent())
			throw new NotFoundException(
					"la categorie dont l' id [ " + articleCmd.getCategorieID() + " ] est inexistante!");
		if (unite == null)
			throw new NotFoundException("l' unité  [ " + articleCmd.getUnite() + " ] est inexistante!");

		Article article = articleMapper.toEntity(articleCmd);

		Article article$ = articleRepository.findByDesignation(article.getDesignation());

		if (article$ != null && article$.getId() != id) {
			if (article$.getShowArt())
				throw new BadRequestException("cette article est deja ajouter!");
			else {
				article$.setShowArt(true);
				article$.setUnite(unite);
				article$.setCategorie(cat.get());

				List<ReceptionAsso> rcAss = receptionAssoRepository.findByArticleId(id);

				if (!rcAss.isEmpty()) {
					rcAss.forEach(as -> {
						as.setArticle(article$);
						receptionAssoRepository.save(as);
					});
				}

				List<ReceptionDesignation> dss = receptionDesignationRepository
						.findDesignationByArticleIDAndFicheNotValid(id);

				if (!dss.isEmpty()) {
					dss.forEach(l -> {
						l.setArticle(article$);
						l.setLibelle(article$.getDesignation());
						l.setUnitee(unite.getUnite());
						receptionDesignationRepository.save(l);
					});
				}

				List<LivraisonDesignation> livs = livraisonDesignationRepository
						.findDesignationByArticleIDAndFicheNotValid(id);

				if (!livs.isEmpty()) {
					livs.forEach(l -> {
						l.setArticleLvr(article$);
						l.setDesignation(article$.getDesignation());
						l.setCategorieLv(article$.getCategorie().getCategorie());
						l.setUnite(article$.getUnite().getUnite());
						livraisonDesignationRepository.save(l);
					});
				}

				articleRepository.deleteById(id);

			}
		} else {
			List<ReceptionDesignation> dss = receptionDesignationRepository
					.findDesignationByArticleIDAndFicheNotValid(id);

			if (!dss.isEmpty()) {
				dss.forEach(l -> {
					l.setLibelle(article.getDesignation());
					l.setUnitee(unite.getUnite());
				});
			}
			article.setShowArt(true);
			article.setUnite(unite);

			article.setId(id);

			return articleRepository.save(article).getId();
		}
		return null;
	}

	public void supprimerCategorie(Integer catId) {
		Optional<CategorieArticle> cat = categorieArticleRepository.findById(catId);

		if (!cat.isPresent())
			throw new NotFoundException("Cette categorie est introuvable!");

		List<Article> articles = receptionAssoRepository.findArticleByCat(cat.get().getCategorie());

		if (!articles.isEmpty())
			throw new BadRequestException("Cette categorie est deja associer des fournisseur!");

		articles.forEach(a -> {
			a.setShowArt(false);
			articleRepository.save(a);
		});

		cat.get().setShowCat(false);

		// categorieArticleRepository.delete(cat.get());

	}

	public void supprimerArticle(Integer artId) {
		Optional<Article> ar = articleRepository.findById(artId);

		if (!ar.isPresent())
			throw new NotFoundException("Cet article est introuvable!");

		List<ReceptionAsso> assos = receptionAssoRepository.findByArticle(ar.get());

		List<LivraisonDesignation> livs = livraisonDesignationRepository
				.findDesignationByArticleIDAndFicheNotValid(ar.get().getId());

		if (!assos.isEmpty())
			throw new BadRequestException("Cette article est deja associer des fournisseur!");

		if (!livs.isEmpty())
			throw new BadRequestException("Cette article est deja associer a une livraison non valide!");

		ar.get().setShowArt(false);

		// articleRepository.delete(ar.get());

	}

	public Integer AddArticle(ArticleCommande articleCmd) {

		Optional<CategorieArticle> cat = categorieArticleRepository.findById(articleCmd.getCategorieID());
		Unite unite = uniteRepository.findByUnite(articleCmd.getUnite());
		if (!cat.isPresent())
			throw new NotFoundException(
					"la categorie dont l' id [ " + articleCmd.getCategorieID() + " ] est inexistante!");
		if (unite == null)
			throw new NotFoundException("l' unité  [ " + articleCmd.getUnite() + " ] est inexistante!");

		Article article = articleMapper.toEntity(articleCmd);

		Article art = articleRepository.findByDesignation(article.getDesignation());

		if (art != null) {
			if (art.getShowArt())
				throw new NotFoundException("l' article  [" + art.getDesignation() + " ] est deja existant!");
			else {
				art.setCategorie(cat.get());
				art.setShowArt(true);
				return art.getId();
			}

		} else {
			article.setUnite(unite);
			article.setCategorie(cat.get());
			article.setShowArt(true);

			return articleRepository.save(article).getId();
		}

	}

	public List<CategorieArticlePresentation> ListerArticle() {

		List<CategorieArticle> cats = categorieArticleRepository.findByShow(true);

		return cats.stream().map(c -> {
			CategorieArticle cc = new CategorieArticle();
			cc.setId(c.getId());
			cc.setCategorie(c.getCategorie());
			cc.setIsAssoWithProjet(c.getIsAssoWithProjet());
			c.getArticles().forEach(a -> {
				if (a.getShowArt()) {
					Article aa = new Article();
					aa.setId(a.getId());
					aa.setDesignation(a.getDesignation());
					aa.setIsAssoWithProjet(a.getIsAssoWithProjet());
					aa.setShowArt(true);
					aa.setUnite(a.getUnite());
					aa.setStockable(a.getStockable());
					cc.getArticles().add(aa);
				}
			});
			return categorieArticleMapper.toRepresentation(cc);
		}).collect(Collectors.toList());

	}

}
