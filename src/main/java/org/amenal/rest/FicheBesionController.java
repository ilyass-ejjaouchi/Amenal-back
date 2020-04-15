package org.amenal.rest;

import java.time.LocalDate;

import javax.websocket.server.PathParam;

import org.amenal.metier.StockMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Besions")
@CrossOrigin("*")
public class FicheBesionController {
	
	@Autowired 
	StockMetier stockMetier;
	
	@RequestMapping(value = "/{projetId}", method = RequestMethod.GET)
	public void getStockDesignationForFicheBsn
	(@PathVariable Integer projetId , @RequestParam(name = "date", required = false)  LocalDate date ) {
		//stockMetier.getStockLigneDesignationForFicheBsn(1, date);

	}
	
	
	
	

}
