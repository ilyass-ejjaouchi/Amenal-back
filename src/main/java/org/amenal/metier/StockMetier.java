package org.amenal.metier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.ReceptionDesignationRepository;

import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Projet;


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
		
		System.out.println("debut");

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
				dp.setQuantite(MinToHrMin(l.getQuantite()));
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

	public List<StockDesignationPresentation> getStockLigneDesignationForFicheBsn(Integer projetId, LocalDate date) {

		List<StockDesignationPresentation> listDs = new ArrayList<StockDesignationPresentation>();

		/************** OUVRIER **************************/
		List<Map<String, Object>> dd = ouvrierDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		if (!dd.isEmpty()) {
			StockPresentation d = new StockPresentation();
			d.setCategorie("MAIN D'OEUVRE");

			dd.forEach(l -> {
				StockDesignationPresentation dp = new StockDesignationPresentation();
				dp.setType(((String) l.get("qual")).toUpperCase());
				dp.setQuantite(MinToHrMin(((Long) l.get("qt")).doubleValue()));
				dp.setUnite("H");
				listDs.add(dp);
			});

		}

		/****************** LOCATION *********************/

		List<StockDs> stockLoc = locationDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		if (!stockLoc.isEmpty()) {

			stockLoc.forEach(l -> {

				StockDesignationPresentation dp = new StockDesignationPresentation();
				dp.setType(l.getType().toUpperCase());
				dp.setQuantite(MinToHrMin(l.getQuantite()));
				dp.setUnite(l.getUnite());
				listDs.add(dp);
			});
		}

		/********************* RECEPTION **********************************/

		List<StockDs> stockRec = receptionDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		if (!stockRec.isEmpty()) {
			for (StockDs s : stockRec) {

				StockDesignationPresentation dp = new StockDesignationPresentation();
				dp.setType(s.getType().toUpperCase());
				dp.setQuantite(s.getQuantite().toString());
				dp.setUnite(s.getUnite());
				listDs.add(dp);

			}
		}

		return listDs;

	}

	/*
	 * public void addOuvrierToStock(Designation ds, String categorie) {
	 * 
	 * Stock stck = stockRepository.findByCategorie(categorie);
	 * 
	 * if (stck == null) { Stock stock = new Stock(); StockDesignation nDs = new
	 * StockDesignation(); List<Designation> desStocker = new
	 * ArrayList<Designation>(); List<StockDesignation> stockDesignations = new
	 * ArrayList<StockDesignation>(); stock.setCategorie(categorie);
	 * nDs.setQuantite(((OuvrierDesignation) ds).getTravail().doubleValue());
	 * nDs.setDesignation(((OuvrierDesignation) ds).getQualification());
	 * nDs.setUnite("H"); desStocker.add(ds); nDs.setDesStocker(desStocker);
	 * stockDesignations.add(nDs); stock.setStockDesignations(stockDesignations); }
	 * else { if (!stck.getStockDesignations().isEmpty()) { StockDesignation dsS =
	 * stck.getStockDesignations().stream().filter(c -> { return
	 * c.getDesignation().equals(((OuvrierDesignation) ds).getQualification());
	 * }).collect(Collectors.toList()).get(0);
	 * 
	 * dsS.setQuantite(dsS.getQuantite() + ((OuvrierDesignation)
	 * ds).getTravail().doubleValue()); dsS.getDesStocker().add(ds);
	 * 
	 * } else { StockDesignation nDs = new StockDesignation(); List<Designation>
	 * desStocker = new ArrayList<Designation>();
	 * nDs.setQuantite(((OuvrierDesignation) ds).getTravail().doubleValue());
	 * nDs.setDesignation(((OuvrierDesignation) ds).getQualification());
	 * nDs.setUnite("H"); desStocker.add(ds); nDs.setDesStocker(desStocker);
	 * stck.getStockDesignations().add(nDs); } }
	 * 
	 * }
	 */

	/*
	 * public void addArticleToStock(ReceptionDesignation ds) {
	 * 
	 * Stock stck = stockRepository.findByCategorie(ds.getCategorie());
	 * 
	 * if (stck == null) { Stock stock = new Stock(); StockDesignation nDs = new
	 * StockDesignation(); List<StockDesignation> StockDesignations = new
	 * ArrayList<StockDesignation>(); List<Designation> desStocker = new
	 * ArrayList<Designation>(); stock.setCategorie(ds.getCategorie());
	 * nDs.setQuantite(ds.getQuantite()); nDs.setDesignation(ds.getLibelle());
	 * nDs.setUnite(ds.getUnite()); desStocker.add(ds);
	 * nDs.setDesStocker(desStocker); StockDesignations.add(nDs);
	 * stock.setStockDesignations(StockDesignations); stockRepository.save(stock); }
	 * else { if (!stck.getStockDesignations().isEmpty()) { StockDesignation dsS =
	 * stck.getStockDesignations().stream().filter(c -> { return
	 * c.getDesignation().equals(ds.getLibelle());
	 * }).collect(Collectors.toList()).get(0);
	 * 
	 * dsS.setQuantite(dsS.getQuantite() + ds.getQuantite());
	 * 
	 * } else { StockDesignation nDs = new StockDesignation(); List<Designation>
	 * desStocker = new ArrayList<Designation>(); nDs.setQuantite(ds.getQuantite());
	 * nDs.setDesignation(ds.getLibelle()); nDs.setUnite(ds.getUnite());
	 * desStocker.add(ds); nDs.setDesStocker(desStocker);
	 * stck.getStockDesignations().add(nDs); } }
	 * 
	 * }
	 */
	private static String MinToHrMin(Double t) {

		int hours = (int) (t / 60); // since both are ints, you get an int
		int minutes = (int) (t % 60);

		String m = String.valueOf(minutes);
		String h = String.valueOf(hours);

		if (hours < 10) {
			h = "0" + hours;
		}
		if (minutes < 10) {
			m = "0" + minutes;
		}

		return h + "h:" + m + "min";
	}
}
