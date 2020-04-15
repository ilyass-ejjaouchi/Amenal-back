package org.amenal.metier;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.amenal.entities.QualificationOuvrier;
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

	@PersistenceContext
	private EntityManager entityManager;

	public void validerFicheStock(Integer ficheId) {

		Optional<StockFiche> stockFiche = stockFicheRepository.findById(ficheId);

		if (!stockFiche.isPresent())
			throw new NotFoundException("La fiche est introuvable!");

		Integer pId = stockFiche.get().getProjet().getId();
		LocalDate date = stockFiche.get().getDate();
		stockFiche.get().setStocks(getStockLigneDesignation(pId, date));

	}

	public List<Stock> getStockLigneDesignation(Integer projetId, LocalDate date) {

		int index = -1;

		List<Stock> OldlistDs = stockRepository.findByprojetIdAndDate(projetId, date.minusDays(1));

		if (OldlistDs == null)
			OldlistDs = new ArrayList<Stock>();

		for (Stock s : OldlistDs) {
			s.getStockDesignations();
			entityManager.detach(s);
		}

		/************** OUVRIER **************************/
		List<Map<String, Object>> dd = ouvrierDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		Stock stockOuv = OldlistDs.stream().filter(l -> {
			return l.getCategorie().equals("MAIN D'OEUVRE");
		}).findFirst().orElse(null);
		if (stockOuv != null) {
			if (!dd.isEmpty()) {
				index++;
				if (!dd.isEmpty())
					dd.forEach(l -> {
						StockDesignation dp = new StockDesignation();
						dp.setDesignation(((QualificationOuvrier) l.get("qual")).getCode());
						dp.setQualifOuvrier(((QualificationOuvrier) l.get("qual")));
						dp.setQuantite(((Double) l.get("qt")));
						dp.setUnite("H");

						StockDesignation dpp = stockOuv.getStockDesignations().stream().filter(ll -> {
							return ll.getQualifOuvrier().getId() == dp.getQualifOuvrier().getId();
						}).findFirst().orElse(null);

						if (dpp == null)
							stockOuv.getStockDesignations().add(dp);
						else {
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
				dp.setQualifOuvrier(((QualificationOuvrier) l.get("qual")));
				dp.setQuantite(((Double) l.get("qt")).doubleValue());
				dp.setUnite("H");
				dp.setStockable(false);

				d.getStockDesignations().add(dp);
			});

			OldlistDs.add(d);
		}

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
					System.out.println("dddd "+mat.getDesignation());
					System.out.println("dddd "+mat.getDesignation());

					System.out.println("dddd "+mat.getDesignation());

					System.out.println("dddd "+mat.getDesignation());

					dp.setDesignation(mat.getDesignation());
					dp.setArticle(mat);
					dp.setUnite(mat.getUnite().getUnite());
					dp.setQuantite(((Double) l.get("somme")));

					StockDesignation dpp = oldStockLoc.getStockDesignations().stream().filter(ll -> {
						return ll.getArticle().getId() == dp.getArticle().getId();
					}).findFirst().orElse(null);

					if (dpp == null)
						stockOuv.getStockDesignations().add(dp);
					else {
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
				dp.setArticle(mat);
				dp.setUnite(mat.getUnite().getUnite());
				dp.setQuantite(((Double) l.get("somme")));
				dp.setStockable(false);
				d2.getStockDesignations().add(dp);
			});
			OldlistDs.add(d2);
		}
		/*
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
					dp.setDesignation(art.getDesignation());
					dp.setQuantite(s.getQuantite());
					dp.setUnite(art.getUnite().getUnite());
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
					dp.setDesignation(art.getDesignation());
					dp.setQuantite(s.getQuantite());
					dp.setUnite(art.getUnite().getUnite());

					dp.setStockable(art.getStockable());
					if (!art.getStockable())
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

							} else
								oldStockRec.getStockDesignations().add(element);

						}
					} else
						OldlistDs.add(stageStock);

				}
			}
		}

		List<Map<String, Object>> lvrDs = livraisonFicheRepository.findLivraisonDesignationByDate(date, projetId);

		for (Stock stk : OldlistDs) {
			if (stk.getCategorie() != "LOCATION" && stk.getCategorie() != "MAIN D'OEUVRE")
				for (StockDesignation ds : stk.getStockDesignations()) {
					for (Map<String, Object> m : lvrDs) {
						if (ds.getArticle().getId() == m.get("artId")) {
							ds.setQuantite(Double.valueOf(
									(new DecimalFormat("##.##").format(ds.getQuantite() - (Double) m.get("qt")))));
							if (ds.getQuantite() <= 0)
								stk.getStockDesignations().remove(ds);
						}
					}
				}
		}
*/
		return OldlistDs;

	}

	public List<StockDesignationPresentation> getStockLigneDesignationForFicheBsn(Integer projetId, LocalDate date) {

		int index = -1;

		List<Stock> OldlistDs = stockRepository.findByprojetIdAndDate(projetId, date.minusDays(1));
		List<StockDesignation> stockBsn = new ArrayList<StockDesignation>();
		if (OldlistDs == null)
			OldlistDs = new ArrayList<Stock>();

		for (Stock s : OldlistDs)
			entityManager.detach(s);

		/************** OUVRIER **************************/
		List<Map<String, Object>> dd = ouvrierDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		Stock stockOuv = OldlistDs.stream().filter(l -> {
			return l.getCategorie().equals("MAIN D'OEUVRE");
		}).findFirst().orElse(null);

		if (stockOuv != null) {
			if (!dd.isEmpty()) {
				index++;
				dd.forEach(l -> {
					StockDesignation dp = new StockDesignation();
					dp.setDesignation(((QualificationOuvrier) l.get("qual")).getCode());
					dp.setQualifOuvrier(((QualificationOuvrier) l.get("qual")));
					dp.setQuantite(((Double) l.get("qt")));
					dp.setUnite("H");
					dp.setBesionType("ouvrier");
					StockDesignation dpp = stockOuv.getStockDesignations().stream().filter(ll -> {
						return ll.getQualifOuvrier().getId() == dp.getQualifOuvrier().getId();
					}).findFirst().orElse(null);

					if (dpp != null)
						dp.setQuantite(dpp.getQuantite() + dp.getQuantite());

					stockBsn.add(dp);
				});

			}
		} else if (!dd.isEmpty()) {
			index++;
			dd.forEach(l -> {
				StockDesignation dp = new StockDesignation();
				dp.setDesignation(((QualificationOuvrier) l.get("qual")).getCode());
				dp.setQualifOuvrier(((QualificationOuvrier) l.get("qual")));
				dp.setQuantite(((Double) l.get("qt")).doubleValue());
				dp.setUnite("H");
				dp.setStockable(false);

				stockBsn.add(dp);
			});

		}

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
					dp.setArticle(mat);
					dp.setUnite(mat.getUnite().getUnite());
					dp.setQuantite(((Double) l.get("somme")));
					dp.setBesionType("article");

					StockDesignation dpp = oldStockLoc.getStockDesignations().stream().filter(ll -> {
						return ll.getArticle().getId() == dp.getArticle().getId();
					}).findFirst().orElse(null);

					if (dpp != null)
						dp.setQuantite(dpp.getQuantite() + dp.getQuantite());

					stockBsn.add(dp);

				});

		} else if (!stockLoc.isEmpty()) {
			index++;

			stockLoc.forEach(l -> {
				StockDesignation dp = new StockDesignation();
				Article mat = (Article) l.get("mat");
				dp.setDesignation(mat.getDesignation());
				dp.setArticle(mat);
				dp.setUnite(mat.getUnite().getUnite());
				dp.setQuantite(((Double) l.get("somme")));
				dp.setStockable(false);
				dp.setBesionType("article");
				stockBsn.add(dp);
			});
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add("LOCATION");
		list.add("MAIN D'OEUVRE");

		List<StockDesignation> OldlistDss = OldlistDs.stream().filter(l -> {
			return !list.contains(l.getCategorie());
		}).map(l -> l.getStockDesignations()).flatMap(Collection::stream).collect(Collectors.toList());

		for (StockDesignation s : OldlistDss)
			entityManager.detach(s);

		List<StockDs> stockRec = receptionDesignationRepository.findDesignationByDateAndProjet(projetId, date);

		List<Map<String, Object>> lvrDs = livraisonFicheRepository.findLivraisonDesignationByDate(date, projetId);

		if (!stockRec.isEmpty()) {
			for (StockDs s : stockRec) {
				StockDesignation ds = OldlistDss.stream().filter(l -> {
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
				}
				if (!lvrDs.isEmpty())
					for (Map<String, Object> m : lvrDs)
						if (ds.getArticle().getId() == m.get("artId"))
							ds.setQuantite(Double.valueOf(
									(new DecimalFormat("##.##").format(ds.getQuantite() - (Double) m.get("qt")))));

				if (ds.getQuantite() > 0)
					stockBsn.add(ds);

			}
		}

		return stockBsn.stream().map(o -> stockDesignationMapper.toRepresentation(o)).collect(Collectors.toList());

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
		System.out.println("receptionDesignationRepository");
		System.out.println("receptionDesignationRepository");

		System.out.println("receptionDesignationRepository");

		List<StockDs> stockRec = receptionDesignationRepository.findDesignationByDateAndProjet(projetId, date);


		List<Map<String, Object>> lvrDs = livraisonFicheRepository.findLivraisonDesignationByDate(date, projetId);
		System.out.println("livraisonFicheRepository");
		System.out.println("livraisonFicheRepository");
		System.out.println("livraisonFicheRepository");

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
			}
		}

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
