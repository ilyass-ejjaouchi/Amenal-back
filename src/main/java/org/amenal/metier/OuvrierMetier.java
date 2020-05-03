package org.amenal.metier;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.OuvrierRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.QualificationOuvrierRepository;
import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.mapper.OuvrierMapper;
import org.amenal.rest.representation.OuvrierPresentation;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
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
public class OuvrierMetier {

	@Autowired
	OuvrierRepository ouvrierRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	OuvrierDesignationRepository ouvrierDesignationRepository;

	@Autowired
	QualificationOuvrierRepository qualificationOuvrierRepository;

	@Autowired
	OuvrierMapper ouvrierMapper;

	@Autowired
	ProjetMetier projetMetier;

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
			if (i > 1 && ouvrierRepository.findByCin(row.getCell(0).getStringCellValue().toUpperCase()) == null) {
				Ouvrier ouv = new Ouvrier();

				if (row.getCell(0).getCellTypeEnum() == CellType.STRING)
					ouv.setCin(row.getCell(0).getStringCellValue());
				else
					throw new BadRequestException("la colonne CIN dois etre en format chacractaire (" + i + ",1)");
				if (row.getCell(1).getCellTypeEnum() == CellType.STRING)
					ouv.setPrenom(row.getCell(1).getStringCellValue());
				else
					throw new BadRequestException("la colonne PRENOM dois etre en format chacractaire (" + i + ",2)");
				if (row.getCell(2).getCellTypeEnum() == CellType.STRING)
					ouv.setNom(row.getCell(2).getStringCellValue());
				else
					throw new BadRequestException("la colonne NOM dois etre en format chacractaire (" + i + ",3)");
				if (row.getCell(3).getCellTypeEnum() == CellType.STRING) {

					QualificationOuvrier qual = qualificationOuvrierRepository
							.findByCode(row.getCell(3).getStringCellValue().toUpperCase());
					if (qual == null) {
						qual = new QualificationOuvrier();
						qual.setCode(row.getCell(3).getStringCellValue().toUpperCase());
					}
					ouv.setQualification(qual);

				} else
					throw new BadRequestException(
							"la colonne QUALIFICATION dois etre en format chacractaire (" + i + ",4)");
				if (DateUtil.isCellDateFormatted(row.getCell(4))) {
					Date date = row.getCell(4).getDateCellValue();
					System.out.println("DDDDDDDDDDDDD  " + row.getCell(4).getDateCellValue() + "  "
							+ row.getCell(5).getDateCellValue());
					ouv.setDateNaissance(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				} else
					throw new BadRequestException(
							"la colonne DATE_NAISSANCE dois etre en format yyyy/MM/dd (" + i + ",5)");
				if (DateUtil.isCellDateFormatted(row.getCell(5))) {

					Date date = row.getCell(5).getDateCellValue();

					ouv.setDateRecrutement(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				} else
					throw new BadRequestException(
							"la colonne DATE_RECRUTEMENT dois etre en format yyyy/MM/dd (" + i + ",6)");
				if (row.getCell(6).getCellTypeEnum() == CellType.NUMERIC) {
					ouv.setTele(
							String.valueOf(new DecimalFormat("##.##").format(row.getCell(6).getNumericCellValue())));
				} else
					throw new BadRequestException("la colonne TELEPHONE dois etre en format numerique (" + i + ",7)");
				if (row.getCell(7).getCellTypeEnum() == CellType.STRING) {
					ouv.setAppreciation(row.getCell(7).getStringCellValue());
				} else
					throw new BadRequestException(
							"la colonne APPRECIATION dois etre en format chacractaire (" + i + ",8)");
				ouvrierRepository.save(ouv);
			}
			i++;

		}

	}

	public Ouvrier ajouterOuvrier(OuvrierCommande ouvrierCmd) {

		/*
		 * exception...
		 */
		QualificationOuvrier qualification = qualificationOuvrierRepository.findByCode(ouvrierCmd.getQualification());

		if (qualification == null)
			throw new NotFoundException(
					"La qualification dont l' id [ " + ouvrierCmd.getQualification() + " ] est introuvable!");

		Ouvrier ouvrier = ouvrierMapper.toEntity(ouvrierCmd);
		ouvrier.setQualification(qualification);

		Ouvrier oo = ouvrierRepository.findByCin(ouvrier.getCin());

		if (oo != null)
			throw new NotFoundException("Il existe deja un ouvrier dont le CIN est [ " + oo + " ] !");

		oo = ouvrierRepository.findByNomAndPrenom(ouvrier.getNom(), ouvrier.getPrenom());

		if (oo != null)
			throw new NotFoundException("Il existe deja un ouvrier qui porte le nom/prenom est [ " + ouvrier.getNom()
					+ "/" + ouvrier.getPrenom() + " ] !");

		return ouvrierRepository.save(ouvrier);

	}

	public Ouvrier modifierOuvrier(OuvrierCommande ouvrierCmd, Integer id) {

		Ouvrier ouvrier = ouvrierMapper.toEntity(ouvrierCmd);

		Ouvrier oo = ouvrierRepository.findByCin(ouvrier.getCin());

		if (oo != null && oo.getId() != id)
			throw new NotFoundException("Il existe deja un ouvrier dont le CIN est [ " + ouvrier.getCin() + " ] !");

		oo = ouvrierRepository.findByNomAndPrenom(ouvrier.getNom(), ouvrier.getPrenom());

		if (oo != null && oo.getId() != id)
			throw new NotFoundException("Il existe deja un ouvrier qui porte le nom/prenom est [ " + ouvrier.getNom()
					+ "/" + ouvrier.getPrenom() + " ] !");

		if (!ouvrierRepository.findById(id).isPresent())
			throw new NotFoundException("cet ouvrier est inexistant");

		QualificationOuvrier qualification = qualificationOuvrierRepository
				.findByCode(ouvrier.getQualification().getCode());

		if (qualification == null)
			throw new NotFoundException(
					"La qualification [ " + ouvrier.getQualification().getCode() + " ] est introuvable!");

		ouvrier.setId(id);
		ouvrier.setQualification(qualification);

		Ouvrier ouv = ouvrierRepository.save(ouvrier);

		List<OuvrierDesignation> Dss = ouvrierDesignationRepository.findDesignationByOuvrierIDAndFicheNotValid(id);
		if (!Dss.isEmpty())
			Dss.forEach(d -> {
				d.setCin(ouvrier.getCin());
				d.setNom(ouvrier.getNom() + " " + ouvrier.getPrenom());
				d.setQualification(ouvrier.getQualification().getCode());
			});

		return ouv;

	}

	public List<OuvrierPresentation> ListerOuvriers() {

		return ouvrierRepository.findAll().stream().map(o -> ouvrierMapper.toRepresentation(o))
				.collect(Collectors.toList());

	}

	public void SupprimerOuvrier(Integer OuvId) {

		Optional<Ouvrier> ouv = this.ouvrierRepository.findById(OuvId);
		if (!ouv.isPresent())
			throw (new NotFoundException("L'ouvrier [" + OuvId + "] introuvable"));

		List<OuvrierDesignation> Dss = ouvrierDesignationRepository.findDesignationByOuvrierIDAndFicheNotValid(OuvId);

		if (!Dss.isEmpty())
			throw new BadRequestException("L'ouvrier [ " + ouv.get().getNom() + " " + ouv.get().getPrenom()
					+ " ] est deja associer a des fiche non valide");

		List<OuvrierDesignation> ouvDSs = ouvrierDesignationRepository.findDesignationByOuvrierID(OuvId);
		ouvDSs.forEach(ouvDs -> {
			if (ouvDs != null)
				ouvDs.setOuvrier(null);
		});

		List<Projet> ps = projetRepository.findProjetByOuvrierID(OuvId);

		if (!ps.isEmpty())
			ps.forEach(p -> projetMetier.AssocierOuvrierProjet(OuvId, p.getId()));

		this.ouvrierRepository.deleteById(OuvId);

	}

}
