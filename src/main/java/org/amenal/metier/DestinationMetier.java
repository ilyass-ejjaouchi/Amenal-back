package org.amenal.metier;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.DestinationRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.Destination;
import org.amenal.entities.Projet;
import org.amenal.entities.Unite;
import org.amenal.entities.designations.Designation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.representation.DestinationRepresentation;
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
public class DestinationMetier {

	@Autowired
	DestinationRepository destinationRepository;

	@Autowired
	ProjetRepository projetRepository;

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
			if (i > 1 && destinationRepository
					.findByDestination(row.getCell(0).getStringCellValue().toUpperCase()) == null) {
				Destination destination = new Destination();

				if (row.getCell(0).getCellTypeEnum() == CellType.STRING)
					destination.setDestination(row.getCell(0).getStringCellValue());
				else
					throw new BadRequestException(
							"la colonne DESTINATION dois etre en format chacractaire (" + i + ",1)");

				destinationRepository.save(destination);

			}
			i++;

		}

	}

	public void addDestination(String dst) {

		if (dst.trim().isEmpty())
			throw new NotFoundException("Destination invalide!");

		Destination Ds = new Destination();

		Ds.setDestination(dst.trim());
		Destination d = destinationRepository.findByDestination(Ds.getDestination());

		if (d != null)
			throw new BadRequestException("Cette destination est deja existante!");

		destinationRepository.save(Ds);
	}

	public void UpdateDestination(String dst, Integer id) {

		Optional<Destination> d = destinationRepository.findById(id);

		if (!d.isPresent())
			throw new NotFoundException("Destination invalide!");

		Destination d$ = destinationRepository.findByDestination(d.get().getDestination());

		if (d$ != null)
			throw new BadRequestException("Cette destination est deja existante!");

		d.get().setDestination(dst.trim());
	}

	public void DeleteDestination(Integer id) {
		Optional<Destination> d = destinationRepository.findById(id);

		if (!d.isPresent())
			throw new NotFoundException("Destination invalide!");

		if (!d.get().getProjets().isEmpty())
			throw new BadRequestException(
					"La desitnation ne peut pas etre supprimé , elle est deja associer a un ou plusieurs projet");

		d.get().getLivraisonDesignations().forEach(l -> {
			if (!l.getFiche().getIsValidated()) {
				throw new BadRequestException(
						"La desitnation ne peut pas etre supprimé , elle est deja associer a une fiche du projet qui ' est pas valide");
			}
		});

		destinationRepository.deleteById(id);
	}

	public List<DestinationRepresentation> ListerDestination(Integer pid) {
		return destinationRepository.findAll().stream().map(o -> {
			List<Integer> ids = o.getProjets().stream().map(v -> v.getId()).collect(Collectors.toList());

			DestinationRepresentation dstP = new DestinationRepresentation();
			dstP.setId(o.getId());
			dstP.setDestination(o.getDestination());
			if (ids.contains(pid))
				dstP.setIsAsso(true);
			else
				dstP.setIsAsso(false);
			return dstP;
		}).collect(Collectors.toList());

	}

	public List<DestinationRepresentation> ListerDestinationAssoToProjet(Integer projetId) {

		Optional<Projet> projet = projetRepository.findById(projetId);

		if (!projet.isPresent())
			throw new NotFoundException("le projet [" + projetId + "] est inexistant");

		return projet.get().getDestinations().stream().map(o -> {
			DestinationRepresentation dstP = new DestinationRepresentation();
			dstP.setId(o.getId());
			dstP.setDestination(o.getDestination());

			return dstP;
		}).collect(Collectors.toList());

	}

	public void AssoDestToProjet(Integer projetId, Integer dstId) {
		Optional<Destination> d = destinationRepository.findById(dstId);

		if (!d.isPresent())
			throw new NotFoundException("Destination invalide!");

		Optional<Projet> projet = projetRepository.findById(projetId);

		if (!projet.isPresent())
			throw new NotFoundException("le projet [" + projetId + "] est inexistant");

		if (projet.get().getDestinations().contains(d.get())) {
			d.get().getLivraisonDesignations().forEach(l -> {
				if (!l.getFiche().getIsValidated() && l.getFiche().getProjet() == projet.get())
					throw new BadRequestException(
							"La destination ne peut pas etre supprimé , elle est deja associer a une fiche du projet qui ' est pas valide");
			});

			projet.get().getDestinations().remove(d.get());

		} else
			projet.get().getDestinations().add(d.get());

	}

}
