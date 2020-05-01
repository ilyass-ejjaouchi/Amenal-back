package org.amenal.rest;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import org.amenal.metier.StockMetier;
import org.amenal.rest.representation.StockDesignationPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = "*")
public class StockController {

	@Autowired
	StockMetier stockMetier;

	@PreAuthorize("@authoritiesService.hasAuthority(#pid ,'USER')")
	@RequestMapping(value = "/Articles/projets/{pid}", method = RequestMethod.GET)
	public List<StockDesignationPresentation> getStockLigneDesignationForFicheLvr(@PathVariable Integer pid,
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
			throws URISyntaxException {
		return stockMetier.getStockLigneDesignationForFicheLvr(pid, date);
	}

}
