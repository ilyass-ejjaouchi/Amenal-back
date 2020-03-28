package org.amenal.rest.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

		List<ReceptionDesignation> receptionDs = receptionDss.stream()
				.sorted(Comparator.comparing(ReceptionDesignation::getCategorie)).collect(Collectors.toList());


		List<CategorieReceptionDesignationPresentation> cats = new ArrayList<CategorieReceptionDesignationPresentation>();

		CategorieReceptionDesignationPresentation c = new CategorieReceptionDesignationPresentation();
		c.setCategorie(receptionDs.get(0).getCategorie());

		cats.add(c);

		int i = 0;
		for (ReceptionDesignation d : receptionDs) {
			if (d.getCategorie().equals(cats.get(i).getCategorie())) {

				ReceptionDesignationPresentation ds = new ReceptionDesignationPresentation();
				ds.setDesignation(d.getLibelle());
				ds.setUnite(d.getUnite());
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
				ds.setUnite(d.getUnite());
				ds.setQuantite(d.getQuantite());
				ds.setFournisseurNom(d.getFournisseurNom());
				ds.setObservation(d.getObservation());
				ds.setId(d.getId());
				cats.get(i).getReceptionDesignation().add(ds);

			}

		}

		return cats;

	}

}
