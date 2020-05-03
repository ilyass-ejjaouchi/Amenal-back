package org.amenal.metier;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.CategorieArticleRepository;
import org.amenal.dao.LocationAssoRepository;
import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.UniteRepository;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.Unite;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.mapper.MaterielMapper;
import org.amenal.rest.representation.MaterielPresentation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
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
public class MaterielMetier {

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	MaterielMapper materielMapper;

	@Autowired
	CategorieArticleRepository categorieArticleRepository;

	@Autowired
	LocationAssoRepository locationAssoRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	LocationDesignationRepository locationDesignationRepository;

	@Autowired
	UniteRepository uniteRepository;

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
				if (row.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {

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

	public void ajouterMateriel(MaterielCommande materielCmd) {

		Unite unite = uniteRepository.findByUnite(materielCmd.getUnite());

		if (unite == null)
			throw new NotFoundException("l' unité  [ " + materielCmd.getUnite() + " ] est inexistante!");

		Article materiel = materielMapper.toEntity(materielCmd);
		materiel.setUnite(unite);
		this.articleRepository.save(materiel);

	}

	public List<MaterielPresentation> ListerMateriel() {

		return this.articleRepository.findAllMateriels().stream().map(o -> materielMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void modifierMateriel(MaterielCommande matCmd, Integer matID) {

		Optional<Article> mtr = articleRepository.findById(matID);
		Unite unite = uniteRepository.findByUnite(matCmd.getUnite());

		if (unite == null)
			throw new NotFoundException("l' unité  [ " + matCmd.getUnite() + " ] est inexistante!");
		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + matID + "] est inexistant");

		matCmd.setId(matID);
		Article a = materielMapper.toEntity(matCmd);
		a.setUnite(unite);

		/*****/
		List<LocationDesignation> ds = locationDesignationRepository.findDesignationByArticleIDAndFicheNotValid(matID);
		if (!ds.isEmpty())
			ds.forEach(d -> {
				d.setLibelle(a.getDesignation());
				d.setUnite(a.getUnite().getUnite());
			});
		/*****/

		this.articleRepository.save(a);
	}

	public void SupprimerMateriel(Integer matID) {

		Optional<Article> mtr = articleRepository.findById(matID);

		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + matID + "] est inexistant");

		List<LocationAsso> locs = locationAssoRepository.findByMateriel(mtr.get());

		if (!locs.isEmpty()) {
			throw new BadRequestException(
					"Vous ne pouvez pas supprimer le materiel [ " + mtr.get().getDesignation() + " ] !");
		} else {
			mtr.get().setUnite(null);
			articleRepository.delete(mtr.get());
		}

	}

}
