package org.amenal.rest;

import java.net.URISyntaxException;
import java.util.List;

import org.amenal.metier.StockMetier;
import org.amenal.rest.representation.StockPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
@CrossOrigin(origins = "*")
public class StockController {

	@Autowired
	StockMetier stockMetier;

	@RequestMapping(value = "", method = RequestMethod.GET)

	public List<StockPresentation> getStockDesignation() throws URISyntaxException {
		return stockMetier.getStockLigneDesignation(1, null);
	}

}
