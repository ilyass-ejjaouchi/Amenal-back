package org.amenal.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.amenal.entities.Projet;
import org.amenal.entities.designations.Designation;
import org.amenal.metier.ArticleMetier;
import org.amenal.metier.DestinationMetier;
import org.amenal.metier.DocumentFicheMetier;
import org.amenal.metier.FicheVisiteurMetier;
import org.amenal.metier.FournisseurMetier;
import org.amenal.metier.MaterielMetier;
import org.amenal.metier.OuvrierMetier;
import org.amenal.rest.commande.ProjetCommande;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
@CrossOrigin(origins = "*")
public class ImportXslController {

	@Autowired
	OuvrierMetier ouvrierMetier;
	
	@Autowired
	MaterielMetier materielMetier;
	
	@Autowired 
	DocumentFicheMetier documentFicheMetier;
	
	@Autowired
	FicheVisiteurMetier ficheVisiteurMetier;
	
	@Autowired
	FournisseurMetier fournisseurMetier;
	
	@Autowired
	ArticleMetier articleMetier;
	
	@Autowired
	DestinationMetier destinationMetier;
	
	@RequestMapping(value = "/destinations", method = RequestMethod.POST)
	public ResponseEntity<Void> importerDestinations(@RequestParam("excelFile") MultipartFile excelFile)
			throws URISyntaxException {
		
		try {
			destinationMetier.emportExcelFile(excelFile);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.created(new URI("/xsl/articles")).build();
	}
	
	@RequestMapping(value = "/articles", method = RequestMethod.POST)
	public ResponseEntity<Void> importerArticle(@RequestParam("excelFile") MultipartFile excelFile)
			throws URISyntaxException {
		
		try {
			articleMetier.emportExcelFile(excelFile);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.created(new URI("/xsl/articles")).build();
	}
	
	
	@RequestMapping(value = "/visiteurs", method = RequestMethod.POST)
	public ResponseEntity<Void> importerVisiteur(@RequestParam("excelFile") MultipartFile excelFile)
			throws URISyntaxException {
		
		try {
			ficheVisiteurMetier.emportExcelFile(excelFile);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.created(new URI("/xsl/visiteurs")).build();
	}
	
	
	@RequestMapping(value = "/documents", method = RequestMethod.POST)
	public ResponseEntity<Void> importerDocument(@RequestParam("excelFile") MultipartFile excelFile)
			throws URISyntaxException {
		
		try {
			documentFicheMetier.emportExcelFile(excelFile);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.created(new URI("/xsl/documents")).build();
	}

	@RequestMapping(value = "/ouvriers", method = RequestMethod.POST)
	public ResponseEntity<Void> importerOuvrier(@RequestParam("excelFile") MultipartFile excelFile)
			throws URISyntaxException {
		
		try {
			ouvrierMetier.emportExcelFile(excelFile);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.created(new URI("/xsl/ouvriers")).build();
	}
	@RequestMapping(value = "/materiels", method = RequestMethod.POST)
	public ResponseEntity<Void> importerMateriel(@RequestParam("excelFile") MultipartFile excelFile)
			throws URISyntaxException {
		
		try {
			materielMetier.emportExcelFile(excelFile);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.created(new URI("/xsl/materiels")).build();
	}

}
