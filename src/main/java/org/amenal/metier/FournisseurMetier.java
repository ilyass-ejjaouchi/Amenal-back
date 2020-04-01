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
import org.amenal.dao.ReceptionDesignationRepository;
import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.LocationAsso;
import org.amenal.entities.Projet;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
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
	ReceptionDesignationRepository receptionDesignationRepository;

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

		LocationAsso locationAssos = locationAssoRepository.findByMaterielAndFourniseurAndProjet(mtr.get(), f.get(),
				null);

		if (locationAssos == null) {
			loc.setMateriel(mtr.get());
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
					loc1.setMateriel(l.getMateriel());
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

		LocationAsso loc = locationAssoRepository.findByMaterielAndFourniseurAndProjet(mtr.get(), f.get(), p.get());

		if (loc == null) {
			AssosierMaterielToFournisseur(idFourniseur, idMateriel);

			loc = new LocationAsso();
			loc.setMateriel(mtr.get());
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
		
		List<ReceptionDesignation> dsRc= receptionDesignationRepository.findDesignationByfournisseurIDAndFicheNotValid(fourID);
		List<LocationDesignation> dsLoc= locationDesignationRepository.findDesignationByfournisseurIDAndFicheNotValid(f.get());
		
		if(!dsRc.isEmpty()) {
			dsRc.forEach(l->{
				l.setFournisseurNom(fr.getFournisseurNom());
			});
		}
		if(!dsLoc.isEmpty()) {
			dsLoc.forEach(l->{
				l.setFournisseurNom(fr.getFournisseurNom());
			});
		}

		fournisseurRepository.save(fr);

	}

	public void supprimerFourniseurFromFicheReception(Integer fourID) {
		Optional<Fournisseur> f = fournisseurRepository.findById(fourID);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + f.get().getFournisseurNom() + "] est inexistant");

		List<ReceptionDesignation> ds = receptionDesignationRepository.findFournisseurAssoToFicheReception(f.get());

		/***/
		if (!ds.isEmpty()) {
			throw new BadRequestException("le fournisseur  [" + f.get().getFournisseurNom()
					+ "] ne peut pas etre supprimé , " + "il est associer a une fiche non valide");
		}

		List<String> projets = receptionAssoRepository.findProjetByfournisseur(f.get());

		if (!projets.isEmpty()) {
			String ps = "";
			for (String p : projets)
				ps = ps + " " + p;
			throw new BadRequestException("le fournisseur  [" + f.get().getFournisseurNom()
					+ "] ne peut pas etre supprimé " + "le fourniseur est associer au projets = [ " + ps + " ]");
		} else {

			f.get().getReceptionAsso().forEach(l -> {

				receptionAssoRepository.delete(l);

			});
		}

		f.get().setReceptionAsso(null);

	}

	public void supprimerFourniseurFromFicheLocation(Integer fourID, Boolean ctn) {
		Optional<Fournisseur> f = fournisseurRepository.findById(fourID);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + fourID + "] est inexistant");

		List<LocationDesignation> ds = locationDesignationRepository.findDesignationByfournisseurIDAndFicheNotValid(f.get());

		if (!ds.isEmpty())
			throw new BadRequestException("vous ne pouvez pas supprimer ce fournisseur [" + f.get().getFournisseurNom()
					+ "] il est deja " + "associer a une fiche non valide! ");
		List<String> pss = locationAssoRepository.findProjetByfournisseur(f.get());

		if (!pss.isEmpty()) {
			String ps = "";
			for (String p : pss) {
				ps = ps + " " + p;
			}
			throw new BadRequestException("vous ne pouvez pas supprimer ce fournisseur [" + f.get().getFournisseurNom()
					+ "] il est deja " + "associer au projet suivant " + ps);
		}

		if (!f.get().getReceptionAsso().isEmpty() && !ctn)
			throw new BadRequestException("Est ce que vous etes sure de vouloire supprimer ce fournisseur [" + fourID
					+ "] il est deja " + "associer dans la partie Reception ");

		f.get().getReceptionAsso().forEach(l -> {

			receptionAssoRepository.deleteById(l.getId());
		});

		f.get().getLocationAssos().forEach(l -> {

			locationAssoRepository.deleteById(l.getId());
		});
		f.get().setLocationAssos(null);
		f.get().setReceptionAsso(null);

		fournisseurRepository.deleteById(fourID);
	}

	public void DesassosierMaterielToFournisseur(Integer fourID, Integer matId) {

		Optional<Fournisseur> f = fournisseurRepository.findById(fourID);
		Optional<Article> ar = articleRepository.findById(matId);

		if (!f.isPresent())
			throw new NotFoundException("le fournisseur [" + fourID + "] est inexistant");
		if (!ar.isPresent())
			throw new NotFoundException("le article [" + fourID + "] est inexistant");

		List<LocationDesignation> ds = locationDesignationRepository.findDesignationByArticleIDAndFicheNotValid(matId);

		if (!ds.isEmpty())
			throw new BadRequestException("vous ne pouvez pas supprimer le materiel [ " + ar.get().getDesignation()
					+ " ] il est deja " + "associer a une fiche non valide! ");

		List<String> pss = locationAssoRepository.findProjetByFourniseurAndMateriel(f.get(), ar.get());
		if (!pss.isEmpty()) {
			String ps = "";
			for (String p : pss) {
				ps = ps + " " + p;
			}
			throw new BadRequestException("vous ne pouvez pas désassocier le materiel [" + ar.get().getDesignation()
					+ "] du fournisseur [" + f.get().getFournisseurNom()
					+ "] cette combinaison deja associer au projets suivant " + ps);
		}

		locationAssoRepository.deleteLocationAssoByFournisseurAndMateriel(f.get(), ar.get());

	}

	private List<MaterielPresentation> smtg(Fournisseur f, Integer idProjet) {
		isAsso = false;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		if (f.getLocationAssos().isEmpty())
			return new ArrayList<MaterielPresentation>();
		List<MaterielPresentation> ms = new ArrayList<MaterielPresentation>();

		f.getLocationAssos().stream().forEach(l -> {

			if (l.getMateriel() != null) {
				MaterielPresentation mp = materielMapper.toRepresentation(l.getMateriel());
				if (l.getProjet() != null) {
					if (l.getProjet().getId() == idProjet)
						this.isAsso = true;
					mp.setIsAssoWithProjet(l.getProjet().getId() == idProjet);
					ids.add(l.getMateriel().getId());
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