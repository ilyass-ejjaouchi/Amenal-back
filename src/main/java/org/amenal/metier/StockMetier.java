package org.amenal.metier;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.rest.representation.StockDesignationPresentation;
import org.amenal.rest.representation.StockPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StockMetier {

	@Autowired
	OuvrierDesignationRepository ouvrierDesignationRepository;

	@Autowired
	LocationDesignationRepository locationDesignationRepository;

	@Autowired
	ReceptionDesignationRepository receptionDesignationRepository;

	public List<StockPresentation> getStockLigneDesignation(Integer projetId, LocalDate date) {

		Projet p = new Projet();
		int index = -1;

		List<StockPresentation> listDs = new ArrayList<StockPresentation>();

		/************** OUVRIER **************************/
		List<Map<String, Object>> dd = ouvrierDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		if (!dd.isEmpty()) {
			index++;
			StockPresentation d = new StockPresentation();
			d.setCategorie("MAIN D'OEUVRE");

			dd.forEach(l -> {
				StockDesignationPresentation dp = new StockDesignationPresentation();
				dp.setType(((String) l.get("qual")).toUpperCase());
				dp.setQuantite(MinToHrMin(((Long) l.get("qt")).doubleValue()));
				dp.setUnite("H");
				d.getStockDesignations().add(dp);
			});

			listDs.add(d);
		}

		/****************** LOCATION *********************/

		List<StockDs> stockLoc = locationDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		if (!stockLoc.isEmpty()) {
			index++;
			StockPresentation d2 = new StockPresentation();
			d2.setCategorie("LOCATION");

			stockLoc.forEach(l -> {

				StockDesignationPresentation dp = new StockDesignationPresentation();
				dp.setType(l.getType().toUpperCase());
				dp.setQuantite( MinToHrMin(l.getQuantite()));
				dp.setUnite(l.getUnite());
				d2.getStockDesignations().add(dp);
			});
			listDs.add(d2);
		}

		/********************* RECEPTION **********************************/

		List<StockDs> stockRec = receptionDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		if (!stockRec.isEmpty()) {
			StockPresentation d2 = new StockPresentation();
			d2.setCategorie(stockRec.get(0).getCategorie().toUpperCase());
			listDs.add(d2);
			index++;
			for (StockDs s : stockRec) {
				if (s.getCategorie().toUpperCase().equals(listDs.get(index).getCategorie())) {

					StockDesignationPresentation dp = new StockDesignationPresentation();
					dp.setType(s.getType().toUpperCase());
					dp.setQuantite(s.getQuantite().toString());
					dp.setUnite(s.getUnite());
					listDs.get(index).getStockDesignations().add(dp);
				} else {

					d2 = new StockPresentation();
					d2.setCategorie(s.getCategorie().toUpperCase());
					listDs.add(d2);
					index++;
					StockDesignationPresentation dp = new StockDesignationPresentation();
					dp.setType(s.getType().toUpperCase());
					dp.setQuantite(s.getQuantite().toString());
					dp.setUnite(s.getUnite());
					listDs.get(index).getStockDesignations().add(dp);
				}
			}
		}

		return listDs;

	}
	
	private static String MinToHrMin(Double t) {
		
		int hours = (int) (t / 60); //since both are ints, you get an int
		int minutes = (int) (t % 60);
		
		String m = String.valueOf(minutes);
		String h = String.valueOf(hours);
		
		if(hours<10) {
			h="0"+hours;
		}
		if(minutes<10) {
			m = "0"+minutes;
		}
		
		return h+"h:"+m+"min" ;
	}
}
