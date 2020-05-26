package org.amenal.metier;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.amenal.dao.ActiviteDesignationRepository;
import org.amenal.dao.LivraisonFicheRepository;
import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.OuvrierDesignationRepository;
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.dao.StockDesignationRepository;
import org.amenal.dao.StockFicheRepository;
import org.amenal.dao.StockRepository;
import org.amenal.dao.pojo.StockDs;
import org.amenal.entities.Article;
import org.amenal.entities.CategorieArticle;
import org.amenal.entities.Entree;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrier;
import org.amenal.entities.designations.EntreeDesignation;
import org.amenal.entities.designations.Stock;
import org.amenal.entities.designations.StockDesignation;
import org.amenal.entities.fiches.StockFiche;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.mapper.StockDesignationMapper;
import org.amenal.rest.mapper.StockMapper;
import org.amenal.rest.representation.StockDesignationPresentation;
import org.hibernate.transform.ToListResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class StockMetier {

	@Autowired
	StockMapper stockMapper;

	@Autowired
	StockDesignationMapper stockDesignationMapper;

	@Autowired
	StockFicheRepository stockFicheRepository;

	@Autowired
	OuvrierDesignationRepository ouvrierDesignationRepository;

	@Autowired
	LocationDesignationRepository locationDesignationRepository;

	@Autowired
	ReceptionDesignationRepository receptionDesignationRepository;

	@Autowired
	StockDesignationRepository stockDesignationRepository;

	@Autowired
	StockRepository stockRepository;

	@Autowired
	LivraisonFicheRepository livraisonFicheRepository;

	@Autowired
	ActiviteDesignationRepository activiteDesignationRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public void validerFicheStock(Integer ficheId) {

		Optional<StockFiche> stockFiche = stockFicheRepository.findById(ficheId);

		if (!stockFiche.isPresent())
			throw new NotFoundException("La fiche est introuvable!");

		stockFiche.get().setIsValidated(true);

		Integer pId = stockFiche.get().getProjet().getId();
		LocalDate date = stockFiche.get().getDate();
		stockFiche.get().setStocks(getStockLigneDesignation(pId, date).stream().filter(o -> o.getStockable())
				.collect(Collectors.toList()));

	}

	public void validerFicheStock(LocalDate date, Projet projet) {

		StockFiche stockFiche = stockFicheRepository.findByDateAndProjet(date, projet);

		if (stockFiche == null)
			throw new NotFoundException("La fiche est introuvable!");

		stockFiche.setIsValidated(true);

		List<Stock> stock = getStockLigneDesignation(projet.getId(), date);
		stockFiche.setStocks(stock);

		for (Stock s : stock) {
			s.setStockDesignations(
					s.getStockDesignations().stream().filter(l -> l.getStockable()).collect(Collectors.toList()));
			s.setStockable(true);
			if (!s.getStockDesignations().isEmpty()) {
				s.setStockFiche(stockFiche);
				stockRepository.save(s);
			}
		}

	}

	public List<Stock> getStockLigneDesignation(Integer projetId, LocalDate date) {

		System.out.println("projet : " + projetId + "  date" + date);

		int index = -1;

		List<Stock> OldlistDs = stockRepository.findByprojetIdAndDate(projetId, date.minusDays(1));

		if (OldlistDs == null)
			OldlistDs = new ArrayList<Stock>();

		for (Stock s : OldlistDs) {
			entityManager.detach(s);
		}
		System.out.println("projet : " + projetId + "  date" + date);

		/************** OUVRIER **************************/
		List<Map<String, Object>> dd = ouvrierDesignationRepository.findDesignationByDateAndProjet(projetId, date);
		System.out.println("****************************  " + dd.size());

		Stock stockOuv = OldlistDs.stream().filter(l -> {
			return l.getCategorie().equals("MAIN D'OEUVRE");
		}).findFirst().orElse(null);
		if (stockOuv != null) {
			if (!dd.isEmpty()) {
				index++;
				dd.forEach(l -> {
					StockDesignation dp = new StockDesignation();
					System.out.println();
					System.out.println("fffff : " + ((QualificationOuvrier) l.get("qual")).getCode());
					System.out.println();
					dp.setDesignation(((QualificationOuvrier) l.get("qual")).getCode());
					dp.setEntreeType(Entree.OUVRIER);
					dp.setQualifOuvrier(((QualificationOuvrier) l.get("qual")));
					dp.setQuantite(((Double) l.get("qt")));
					dp.setUnite("H");

					StockDesignation dpp = stockOuv.getStockDesignations().stream().filter(ll -> {
						return ll.getQualifOuvrier().getId() == dp.getQualifOuvrier().getId();
					}).findFirst().orElse(null);

					if (dpp == null) {
						dp.setStock(stockOuv);
						stockOuv.getStockDesignations().add(dp);
					} else {
						dpp.setStock(stockOuv);
						dpp.setDesignation(dp.getDesignation());
						dpp.setQuantite(dpp.getQuantite() + dp.getQuantite());
					}

				});

			}
		} else if (!dd.isEmpty()) {
			index++;
			Stock d = new Stock();
			d.setCategorie("MAIN D'OEUVRE");
			d.setStockable(false);

			dd.forEach(l -> {
				StockDesignation dp = new StockDesignation();
				dp.setDesignation(((QualificationOuvrier) l.get("qual")).getCode());
				dp.setEntreeType(Entree.OUVRIER);
				dp.setQualifOuvrier(((QualificationOuvrier) l.get("qual")));
				dp.setQuantite(((Double) l.get("qt")).doubleValue());
				dp.setQuantite(Double.valueOf((new DecimalFormat("##.##").format((Double) l.get("qt")))));

				dp.setUnite("H");
				dp.setStockable(false);
				dp.setStock(d);
				d.getStockDesignations().add(dp);
			});

			OldlistDs.add(d);
		}
		System.out.println("OldlistDs : " + OldlistDs.size());

		/****************** LOCATION *********************/

		List<Map<String, Object>> stockLoc = locationDesignationRepository.findDesignationByDateAndProjet(projetId,
				date);
		Stock oldStockLoc = OldlistDs.stream().filter(l -> {
			return l.getCategorie().equals("LOCATION");
		}).findFirst().orElse(null);

		if (oldStockLoc != null) {
			if (!stockLoc.isEmpty())
				stockLoc.forEach(l -> {

					StockDesignation dp = new StockDesignation();
					Article mat = (Article) l.get("mat");

					dp.setDesignation(mat.getDesignation());
					dp.setEntreeType(Entree.ARTICLE);

					dp.setArticle(mat);
					dp.setUnite(mat.getUnite().getUnite());
					dp.setQuantite(((Double) l.get("somme")));

					StockDesignation dpp = oldStockLoc.getStockDesignations().stream().filter(ll -> {
						return ll.getArticle().getId() == dp.getArticle().getId();
					}).findFirst().orElse(null);

					if (dpp == null) {
						dp.setStock(stockOuv);
						stockOuv.getStockDesignations().add(dp);
					} else {
						dpp.setStock(stockOuv);
						dpp.setDesignation(mat.getDesignation());
						dpp.setQuantite(dpp.getQuantite() + dp.getQuantite());
					}

				});

		} else if (!stockLoc.isEmpty()) {
			index++;
			Stock d2 = new Stock();
			d2.setCategorie("LOCATION");
			d2.setStockable(false);

			stockLoc.forEach(l -> {
				StockDesignation dp = new StockDesignation();

				Article mat = (Article) l.get("mat");

				dp.setDesignation(mat.getDesignation());
				dp.setEntreeType(Entree.ARTICLE);
				dp.setArticle(mat);
				dp.setUnite(mat.getUnite().getUnite());
				dp.setQuantite(((Double) l.get("somme")));
				dp.setStockable(false);
				dp.setStock(d2);
				d2.getStockDesignations().add(dp);
			});
			OldlistDs.add(d2);
		}

		/*************** RECEPTION ***************/

		List<StockDs> stockRec = receptionDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		Boolean catIsStockable = false;

		if (!stockRec.isEmpty()) {
			index = 0;
			Stock stageStock = new Stock();
			stageStock.setArtCategorie(new CategorieArticle());
			Boolean last = false;
			for (StockDs s : stockRec) {
				if (s.getCategorie().getId() == stageStock.getArtCategorie().getId()) {

					Article art = s.getArticle();
					StockDesignation dp = new StockDesignation();
					dp.setArticle(art);
					dp.setEntreeType(Entree.ARTICLE);
					dp.setDesignation(art.getDesignation());
					dp.setQuantite(s.getQuantite());
					dp.setUnite(art.getUnite().getUnite());
					dp.setStockable(art.getStockable());
					if (art.getStockable() == null)
						stageStock.setStockable(false);
					else if (!art.getStockable())
						stageStock.setStockable(false);

					stageStock.getStockDesignations().add(dp);
					index = stockRec.indexOf(s);

					if (s.getCategorie() != stockRec.get(index).getCategorie()
							|| s == stockRec.get(stockRec.size() - 1))
						last = true;

				} else {

					stageStock = new Stock();
					stageStock.setCategorie(s.getCategorie().getCategorie());
					stageStock.setArtCategorie(s.getCategorie());
					Article art = s.getArticle();
					StockDesignation dp = new StockDesignation();
					dp.setArticle(art);
					dp.setEntreeType(Entree.ARTICLE);
					dp.setDesignation(art.getDesignation());
					dp.setQuantite(s.getQuantite());
					dp.setUnite(art.getUnite().getUnite());

					dp.setStockable(art.getStockable());
					if (art.getStockable() == null)
						stageStock.setStockable(false);
					else if (!art.getStockable())
						stageStock.setStockable(false);

					stageStock.getStockDesignations().add(dp);
					if (s == stockRec.get(stockRec.size() - 1))
						last = true;
					else if (s.getCategorie().getId() != stockRec.get(index).getCategorie().getId())
						last = true;

				}
				if (last) {
					last = false;
					CategorieArticle cat = stageStock.getArtCategorie();
					Stock oldStockRec = OldlistDs.stream().filter(l -> {
						return l.getArtCategorie().getId() == cat.getId();
					}).findFirst().orElse(null);

					if (oldStockRec != null) {
						oldStockRec.setArtCategorie(cat);
						oldStockRec.setCategorie(cat.getCategorie());
						for (StockDesignation element : stageStock.getStockDesignations()) {

							Integer arId = element.getArticle().getId();
							StockDesignation oldStockRecDs = oldStockRec.getStockDesignations().stream().filter(l -> {
								return l.getArticle().getId() == arId;
							}).findFirst().orElse(null);

							if (oldStockRecDs != null) {
								oldStockRecDs.setDesignation(element.getArticle().getDesignation());
								oldStockRecDs.setQuantite(oldStockRecDs.getQuantite() + element.getQuantite());
								if (oldStockRecDs.getQuantite() <= 0)
									stageStock.getStockDesignations().remove(oldStockRecDs);

							} else {
								oldStockRec.getStockDesignations().add(element);
							}

						}
					} else
						OldlistDs.add(stageStock);

				}
			}
		}

		/* REMOVE LIVRAISON ITEM FROM STOCK */

		List<Map<String, Object>> lvrDs = livraisonFicheRepository.findLivraisonDesignationByDate(date, projetId);

		List<StockDesignation> dsToRemove = new ArrayList<StockDesignation>();
		List<Stock> stkToRemove = new ArrayList<Stock>();

		for (Stock stk : OldlistDs) {
			if (stk.getCategorie() != "LOCATION" && stk.getCategorie() != "MAIN D'OEUVRE") {

				for (StockDesignation ds : stk.getStockDesignations()) {
					ds.setStock(stk);
					for (Map<String, Object> m : lvrDs) {
						if (ds.getArticle().getId() == m.get("artId")) {
							ds.setQuantite(Double.valueOf(
									(new DecimalFormat("##.##").format(ds.getQuantite() - (Double) m.get("qt")))));
							if (ds.getQuantite() <= 0)
								dsToRemove.add(ds);
						}
					}
				}
				if (!dsToRemove.isEmpty()) {
					stk.getStockDesignations().removeAll(dsToRemove);
					if (!stk.getStockDesignations().isEmpty())
						stkToRemove.add(stk);
				}
			}
		}

		if (!stkToRemove.isEmpty())
			OldlistDs.removeAll(stkToRemove);

		/* REMOVE FICHE_ACTIVITE ITEM FROM STOCK */

		/*
		 * Map<String, List<EntreeDesignation>> result = entres.stream()
		 * .collect(Collectors.groupingBy(map -> ((String) map.get("type")),
		 * Collectors.mapping(map -> ((EntreeDesignation) map.get("e")),
		 * Collectors.toList())));
		 */
		stkToRemove = new ArrayList<Stock>();
		dsToRemove = new ArrayList<StockDesignation>();

		List<EntreeDesignation> entrees = activiteDesignationRepository.getentreeficheAct(date, projetId);

		List<StockDesignation> stkcDss = OldlistDs.stream().map(s -> s.getStockDesignations())
				.flatMap(Collection::stream).collect(Collectors.toList());
		if (!entrees.isEmpty()) {
			for (StockDesignation x : stkcDss) {
				for (EntreeDesignation ee : entrees) {
					if (ee.getType().equals(Entree.ARTICLE)) {
						if (x.getStock().getCategorie() != "MAIN D'OEUVRE")
							if (ee.getArticle().getId() == x.getArticle().getId())
								x.setQuantite(Double.valueOf(
										(new DecimalFormat("##.##").format(x.getQuantite() - ee.getQuantite()))));
					} else if (ee.getType().equals(Entree.OUVRIER)) {
						if (x.getStock().getCategorie() == "MAIN D'OEUVRE")
							if (ee.getQualification().getId() == x.getQualifOuvrier().getId())
								x.setQuantite(Double.valueOf(
										(new DecimalFormat("##.##").format(x.getQuantite() - ee.getQuantite()))));
					}
				}
				if (x.getQuantite() < 0)
					dsToRemove.add(x);
			}

			/* REMOVE QUANTITES <0 */
			for (Stock stk : OldlistDs) {
				if (!dsToRemove.isEmpty()) {
					stk.getStockDesignations().removeAll(dsToRemove);
					if (!stk.getStockDesignations().isEmpty())
						stkToRemove.add(stk);
				}
			}
			if (!stkToRemove.isEmpty())
				OldlistDs.removeAll(stkToRemove);
		}

		return OldlistDs;

	}

	public List<StockDesignationPresentation> getStockLigneDesignationForFicheLvr(Integer projetId, LocalDate date) {

		ArrayList<String> list = new ArrayList<String>();
		list.add("LOCATION");
		list.add("MAIN D'OEUVRE");

		List<StockDesignation> OldlistDs = stockRepository.findRecByprojetIdAndDate(projetId, date.minusDays(1), list);
		if (!OldlistDs.isEmpty())
			for (StockDesignation s : OldlistDs) {
				entityManager.detach(s);
			}

		List<StockDs> stockRec = receptionDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		List<Map<String, Object>> lvrDs = livraisonFicheRepository.findLivraisonDesignationByDate(date, projetId);

		List<EntreeDesignation> entrees = activiteDesignationRepository.getentreeficheAct(date, projetId);

		if (!stockRec.isEmpty()) {
			for (StockDs s : stockRec) {
				StockDesignation ds = OldlistDs.stream().filter(l -> {
					return l.getArticle().getId() == s.getArticle().getId();
				}).findFirst().orElse(null);

				if (ds != null) {
					ds.setDesignation(s.getArticle().getDesignation());
					ds.setQuantite(ds.getQuantite() + s.getQuantite());
				} else {
					Article art = s.getArticle();
					ds = new StockDesignation();
					ds.setArticle(art);
					ds.setDesignation(art.getDesignation());
					ds.setQuantite(s.getQuantite());
					ds.setUnite(art.getUnite().getUnite());
					OldlistDs.add(ds);
				}
				if (!lvrDs.isEmpty())
					for (Map<String, Object> m : lvrDs)
						if (ds.getArticle().getId() == m.get("artId")) {
							ds.setQuantite(Double.valueOf(
									(new DecimalFormat("##.##").format(ds.getQuantite() - (Double) m.get("qt")))));
							if (ds.getQuantite() <= 0)
								OldlistDs.remove(ds);
						}
				if (!entrees.isEmpty()) {
					for (EntreeDesignation ee : entrees) {
						if (ee.getType().equals(Entree.ARTICLE)) {
							if (ee.getArticle().getId() == ds.getId())
								ds.setQuantite(Double.valueOf(
										(new DecimalFormat("##.##").format(ds.getQuantite() - ee.getQuantite()))));
						}
						if (ds.getQuantite() <= 0)
							OldlistDs.remove(ds);
					}
				}
			}
		}
		
		System.out.println(" ***************** ");
		System.out.println(OldlistDs.size());
		System.out.println(" ***************** ");


		return OldlistDs.stream().map(o -> stockDesignationMapper.toRepresentation(o)).collect(Collectors.toList());

	}

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
