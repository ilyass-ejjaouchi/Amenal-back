package org.amenal.metier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.dao.ArticleRepository;
import org.amenal.dao.FournisseurRepository;
import org.amenal.dao.LocationAssoRepository;
import org.amenal.dao.LocationDesignationRepository;
import org.amenal.dao.ProjetRepository;
import org.amenal.dao.ReceptionAssoRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.exception.BadRequestException;
import org.amenal.exception.NotFoundException;
import org.amenal.rest.commande.FournisseurCommande;
import org.amenal.rest.mapper.FournisseurMapper;
import org.amenal.rest.mapper.MaterielMapper;
import org.amenal.rest.representation.FournisseurPresentation;
import org.amenal.rest.representation.MaterielPresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.db.dialect.MsSQLDialect;

@Service
@Transactional
public class FournisseurMetier {

	private Boolean isAsso = false;

	@Autowired
	FournisseurRepository fournisseurRepository;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	LocationAssoRepository locationAssoRepository;

	@Autowired
	ProjetRepository projetRepository;

	@Autowired
	ReceptionAssoRepository receptionAssoRepository;

	@Autowired
	FournisseurMapper fournisseurMapper;

	@Autowired
	MaterielMapper materielMapper;

	@Autowired
	LocationDesignationRepository locationDesignationRepository;

	public Fournisseur AjouterFournisseur(FournisseurCommande frCmd) {

		Fournisseur fr = fournisseurMapper.toEntity(frCmd);

		return fournisseurRepository.save(fr);

	}

	public List<FournisseurPresentation> ListerFournisseurByProjetAndMateriel(Integer idProjet, Integer idMat) {

		List<FournisseurPresentation> frPrs = fournisseurRepository.findByProjetIdAndMaterielId(idProjet, idMat)
				.stream().map(o -> {
					FournisseurPresentation frs = fournisseurMapper.toRepresentation(o);
					frs.setMateriel(null);
					frs.setIsAssoWithProjet(true);

					return frs;
				}).collect(Collectors.toList());

		return frPrs;
	}

	public List<FournisseurPresentation> ListerFournisseur(Integer idProjet) {

		List<FournisseurPresentation> frPrs = fournisseurRepository.findByProjetId(idProjet).stream().map(o -> {
			FournisseurPresentation frs = fournisseurMapper.toRepresentation(o);
			frs.setMateriels(smtg(o, idProjet));
			frs.setIsAssoWithProjet(this.isAsso);

			return frs;
		}).collect(Collectors.toList());

		return frPrs;
	}

	public void AssosierMaterielToFournisseur(Integer idFourniseur, Integer idMateriel) {

		Optional<Fournisseur> f = fournisseurRepository.findById(idFourniseur);
		Optional<Article> mtr = articleRepository.findById(idMateriel);
		LocationAsso loc = new LocationAsso();

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + idFourniseur + "] est inexistant");
		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + idMateriel + "] est inexistant");

		LocationAsso locationAssos = locationAssoRepository.findByArticleAndFourniseurAndProjet(mtr.get(), f.get(),
				null);

		if (locationAssos == null) {
			loc.setArticle(mtr.get());
			loc.setFourniseur(f.get());
			locationAssoRepository.save(loc);
		}
	}

	public void AssosierFournisseurToProjet(Integer idFourniseur, Integer idProjet) {

		Optional<Fournisseur> f = fournisseurRepository.findById(idFourniseur);
		Optional<Projet> projet = projetRepository.findById(idProjet);
		LocationAsso loc = new LocationAsso();

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + idFourniseur + "] est inexistant");
		if (!projet.isPresent())
			throw new NotFoundException("le projet [" + idProjet + "] est inexistant");

		List<LocationAsso> locationAssos = locationAssoRepository.findByFourniseurAndProjet(f.get(), projet.get());

		if (locationAssos.isEmpty()) {

			locationAssos = locationAssoRepository.findByFourniseur(f.get());
			if (!locationAssos.isEmpty()) {
				locationAssos.forEach(l -> {
					LocationAsso loc1 = new LocationAsso();
					loc1.setArticle(l.getArticle());
					loc1.setFourniseur(f.get());
					loc1.setProjet(projet.get());
					locationAssoRepository.save(loc1);

				});
			} else {

				loc.setFourniseur(f.get());
				loc.setProjet(projet.get());
				locationAssoRepository.save(loc);
			}

		} else {
			locationAssos.stream().forEach(l -> {
				locationAssoRepository.delete(l);

			});
		}
	}

	public void AssosierMaterielToFournisseurToProjet(Integer idFourniseur, Integer idMateriel, Integer idProjet) {

		Optional<Fournisseur> f = fournisseurRepository.findById(idFourniseur);
		Optional<Article> mtr = articleRepository.findById(idMateriel);
		Optional<Projet> p = projetRepository.findById(idProjet);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + idFourniseur + "] est inexistant");

		if (!mtr.isPresent())
			throw new NotFoundException("l'article [" + idMateriel + "] est inexistant");

		if (!p.isPresent())
			throw new NotFoundException(" le projet [" + idProjet + "] est inexistant");

		LocationAsso loc = locationAssoRepository.findByArticleAndFourniseurAndProjet(mtr.get(), f.get(), p.get());

		if (loc == null) {
			AssosierMaterielToFournisseur(idFourniseur, idMateriel);

			loc = new LocationAsso();
			loc.setArticle(mtr.get());
			loc.setFourniseur(f.get());
			loc.setProjet(p.get());
			locationAssoRepository.save(loc);
		} else
			locationAssoRepository.delete(loc);

	}

	public void modifierFourniseur(FournisseurCommande fourCmd, Integer fourID) {

		Optional<Fournisseur> f = fournisseurRepository.findById(fourID);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + fourID + "] est inexistant");
		
		Fournisseur fr = fournisseurMapper.toEntity(fourCmd);
		fr.setId(fourID);

		fournisseurRepository.save(fr);

	}
	public void supprimerFourniseurFromFicheReception(Integer fourID, Boolean ctn) {
		Optional<Fournisseur> f = fournisseurRepository.findById(fourID);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + fourID + "] est inexistant");

		List<LocationAsso> ass = locationAssoRepository.findByFourniseur(f.get());
		List<LocationDesignation> ds = locationDesignationRepository.findFournisseurAssoToFiche(f.get());

		if (!ds.isEmpty())
			throw new BadRequestException("vous ne pouvez pas supprimer ce fournisseur [" + fourID + "] il est deja "
					+ "associer a une fiche non valide! ");

		if (!f.get().getReceptionAsso().isEmpty() && !ctn)
			throw new BadRequestException("Est ce que vous etes sure de vouloire supprimer ce fournisseur [" + fourID
					+ "] il est deja " + "associer dans la partie Reception ");

		f.get().getReceptionAsso().forEach(l -> {

			receptionAssoRepository.deleteById(l.getId());
		});

		ass.forEach(l -> {

			locationAssoRepository.deleteById(l.getId());
		});
		f.get().setLocationAssos(null);
		f.get().setReceptionAsso(null);

		fournisseurRepository.deleteById(fourID);
	}


	public void supprimerFourniseurFromFicheLocation(Integer fourID, Boolean ctn) {
		Optional<Fournisseur> f = fournisseurRepository.findById(fourID);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + fourID + "] est inexistant");

		List<LocationAsso> ass = locationAssoRepository.findByFourniseur(f.get());
		List<LocationDesignation> ds = locationDesignationRepository.findFournisseurAssoToFiche(f.get());

		if (!ds.isEmpty())
			throw new BadRequestException("vous ne pouvez pas supprimer ce fournisseur [" + fourID + "] il est deja "
					+ "associer a une fiche non valide! ");

		if (!f.get().getReceptionAsso().isEmpty() && !ctn)
			throw new BadRequestException("Est ce que vous etes sure de vouloire supprimer ce fournisseur [" + fourID
					+ "] il est deja " + "associer dans la partie Reception ");

		f.get().getReceptionAsso().forEach(l -> {

			receptionAssoRepository.deleteById(l.getId());
		});

		ass.forEach(l -> {

			locationAssoRepository.deleteById(l.getId());
		});
		f.get().setLocationAssos(null);
		f.get().setReceptionAsso(null);

		fournisseurRepository.deleteById(fourID);
	}

	private List<MaterielPresentation> smtg(Fournisseur f, Integer idProjet) {
		isAsso = false;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		if (f.getLocationAssos().isEmpty())
			return new ArrayList<MaterielPresentation>();
		List<MaterielPresentation> ms = new ArrayList<MaterielPresentation>();

		f.getLocationAssos().stream().forEach(l -> {

			if (l.getArticle() != null) {
				MaterielPresentation mp = materielMapper.toRepresentation(l.getArticle());
				if (l.getProjet() != null) {
					if (l.getProjet().getId() == idProjet)
						this.isAsso = true;
					mp.setIsAssoWithProjet(l.getProjet().getId() == idProjet);
					ids.add(l.getArticle().getId());
				} else
					mp.setIsAssoWithProjet(false);
				ms.add(mp);

			} else {
				if (l.getProjet().getId() == idProjet)
					this.isAsso = true;
			}

		});

		return (ms.stream().filter(m -> {
			return (ids.contains(m.getId()) && m.getIsAssoWithProjet()) || !ids.contains(m.getId());
		}).collect(Collectors.toList()));

	}

}