package org.amenal.rest.mapper;

import java.io.Console;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.rest.representation.CategorieReceptionDesignationPresentation;
import org.amenal.rest.representation.FichePresentation;
import org.amenal.rest.representation.ReceptionDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FicheReceptionMapper {

	@Mapping(source = "e.receptionDesignations", target = "categories", qualifiedByName = "toCat")
	FichePresentation toRepresentation(ReceptionFiche e);

	@Named("toCat")
	default List<CategorieReceptionDesignationPresentation> toCat(List<ReceptionDesignation> receptionDss) {
		List<CategorieReceptionDesignationPresentation> cats = new ArrayList<CategorieReceptionDesignationPresentation>();

		int index = 0;

		if (!receptionDss.isEmpty()) {
			List<ReceptionDesignation> receptionDs = receptionDss.stream()
					.sorted(Comparator.comparing(ReceptionDesignation::getCategorie)).collect(Collectors.toList());

			/***/

			Optional<ReceptionDesignation> recOuv = receptionDs.stream()
					.filter(r -> r.getCategorie().equals("MAIN D'OEUVRE")).findFirst();

			if (recOuv.isPresent()) {
				CategorieReceptionDesignationPresentation cat = new CategorieReceptionDesignationPresentation();

				cat.setCategorie("MAIN D'OEUVRE");

				List<OuvrierDesignation> list = recOuv.get().getOuvrierDesignations().stream()
						.sorted(Comparator.comparing(OuvrierDesignation::getQualification))
						.collect(Collectors.toList());

				int i = -1;

				for (OuvrierDesignation ouvDs : list) {
					if (!cat.getReceptionDesignation().isEmpty()) {
						if (ouvDs.getQualification().equals(cat.getReceptionDesignation().get(i).getDesignation()))
							cat.getReceptionDesignation().get(i).setQuantite(
									ouvDs.getTravail() + cat.getReceptionDesignation().get(i).getQuantite());
					} else {
						ReceptionDesignationPresentation ds = new ReceptionDesignationPresentation();
						ds.setDesignation(ouvDs.getQualification());
						ds.setUnite("H");
						ds.setQuantite(ouvDs.getTravail().doubleValue());
						ds.setFournisseurNom("PAS DE FOURNISSEUR");
						ds.setObservation("Rien a signaler");
						cat.getReceptionDesignation().add(ds);
						i++;
					}
				}
				cats.add(cat);
				index++;
				receptionDs.remove(recOuv.get());

			}

			/****/

			Optional<ReceptionDesignation> recLoc = receptionDs.stream()
					.filter(r -> r.getCategorie().equals("LOCATION")).findFirst();

			if (recLoc.isPresent()) {
				CategorieReceptionDesignationPresentation cat = new CategorieReceptionDesignationPresentation();

				cat.setCategorie("LOCATION");

				List<LocationDesignation> list = recLoc.get().getLocationDesignations().stream()
						.sorted(Comparator.comparing(LocationDesignation::getLibelle)).collect(Collectors.toList());

				int i = -1;

				for (LocationDesignation locDs : list) {
					if (!cat.getReceptionDesignation().isEmpty()) {
						if (locDs.getLibelle().equals(cat.getReceptionDesignation().get(i).getDesignation()))
							cat.getReceptionDesignation().get(i).setQuantite(
									locDs.getTravailleLoc() + cat.getReceptionDesignation().get(i).getQuantite());
					} else {
						ReceptionDesignationPresentation ds = new ReceptionDesignationPresentation();
						System.out.println("ssssssss  :  "+locDs.getLibelle());
						ds.setDesignation(locDs.getLibelle());
						ds.setUnite("H");
						ds.setQuantite(locDs.getTravailleLoc().doubleValue());
						ds.setFournisseurNom(locDs.getFournisseurNom());
						ds.setObservation("Rien a signaler");
						cat.getReceptionDesignation().add(ds);
						i++;
					}
				}
				cats.add(cat);
				index++;
				receptionDs.remove(recLoc.get());

			}

			/*********/

			CategorieReceptionDesignationPresentation c = new CategorieReceptionDesignationPresentation();

			if (!receptionDs.isEmpty()) {
				c.setCategorie(receptionDs.get(0).getCategorie());

				cats.add(c);

				int i = index;
				for (ReceptionDesignation d : receptionDs) {
					if (d.getCategorie().equals(cats.get(i).getCategorie())) {

						ReceptionDesignationPresentation ds = new ReceptionDesignationPresentation();
						ds.setDesignation(d.getLibelle());
						ds.setUnite(d.getUnitee());
						ds.setQuantite(d.getQuantite());
						ds.setFournisseurNom(d.getFournisseurNom());
						ds.setObservation(d.getObservation());
						ds.setId(d.getId());
						cats.get(i).getReceptionDesignation().add(ds);

					} else {

						ReceptionDesignationPresentation ds = new ReceptionDesignationPresentation();

						c = new CategorieReceptionDesignationPresentation();
						c.setCategorie(d.getCategorie());
						cats.add(c);
						i++;

						ds.setDesignation(d.getLibelle());
						ds.setUnite(d.getUnitee());
						ds.setQuantite(d.getQuantite());
						ds.setFournisseurNom(d.getFournisseurNom());
						ds.setObservation(d.getObservation());
						ds.setId(d.getId());
						cats.get(i).getReceptionDesignation().add(ds);

					}

				}
			}

		}

		return cats;

	}

}
