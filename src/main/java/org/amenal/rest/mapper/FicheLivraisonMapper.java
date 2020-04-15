package org.amenal.rest.mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.rest.representation.CategorieLivraisonDesignationPresentation;
import org.amenal.rest.representation.CategorieReceptionDesignationPresentation;
import org.amenal.rest.representation.FichePresentation;
import org.amenal.rest.representation.LivraisonDesignationPresentation;
import org.amenal.rest.representation.ReceptionDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = LivraisonDesignationMapper.class)
public interface FicheLivraisonMapper {

	@Mapping(source = "e.livraisonDesignations", target = "categorieLivraisons", qualifiedByName = "toCat")
	FichePresentation toRepresentation(LivraisonFiche e);

	@Named("toCat")
	default List<CategorieLivraisonDesignationPresentation> toCat(List<LivraisonDesignation> livDss) {
		List<CategorieLivraisonDesignationPresentation> cats = new ArrayList<CategorieLivraisonDesignationPresentation>();


		if (!livDss.isEmpty()) {
			List<LivraisonDesignation> livDs = livDss.stream()
					.sorted(Comparator.comparing(LivraisonDesignation::getCategorieLv)).collect(Collectors.toList());

			/*********/

			CategorieLivraisonDesignationPresentation c = new CategorieLivraisonDesignationPresentation();

			if (!livDs.isEmpty()) {
				c.setCategorie(livDs.get(0).getCategorieLv());
				cats.add(c);
				System.out.println("ddddddddddddddddddddddddddddddd : "+cats.get(0).getCategorie());
				int i = 0;
				for (LivraisonDesignation d : livDs) {
					if (d.getCategorieLv().equals(cats.get(i).getCategorie())) {

						LivraisonDesignationPresentation ds = new LivraisonDesignationPresentation();
						ds.setDesignation(d.getDesignation());
						ds.setUnite(d.getUnite());
						ds.setArticleId(d.getArticleLvr().getId());
						ds.setQuantite(d.getQuantite());
						ds.setDestinationNom(d.getDestinationNom());
						ds.setDestinationId(d.getDestination().getId());
						ds.setObservation(d.getObservation());
						ds.setId(d.getId());
						cats.get(i).getLivraisonDesignationPresentations().add(ds);

					} else {

						LivraisonDesignationPresentation ds = new LivraisonDesignationPresentation();

						c = new CategorieLivraisonDesignationPresentation();
						c.setCategorie(d.getCategorieLv());
						cats.add(c);
						i++;

						ds.setDesignation(d.getDesignation());
						ds.setUnite(d.getUnite());
						ds.setArticleId(d.getArticleLvr().getId());
						ds.setQuantite(d.getQuantite());
						ds.setDestinationNom(d.getDestinationNom());
						ds.setDestinationId(d.getDestination().getId());
						ds.setObservation(d.getObservation());
						ds.setId(d.getId());
						cats.get(i).getLivraisonDesignationPresentations().add(ds);

					}

				}
			}

		}

		return cats;

	}

}
