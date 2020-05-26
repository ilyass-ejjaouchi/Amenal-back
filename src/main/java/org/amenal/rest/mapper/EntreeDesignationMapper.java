package org.amenal.rest.mapper;

import org.amenal.entities.Entree;
import org.amenal.entities.designations.EntreeDesignation;
import org.amenal.rest.representation.EntreeDesignationPresentation;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EntreeDesignationMapper {

	EntreeDesignationPresentation toRepresentation(EntreeDesignation e);

	@AfterMapping
	default void afterMapping(EntreeDesignation e, @MappingTarget EntreeDesignationPresentation prs) {

		if (e.getType().equals(Entree.OUVRIER)) {
			prs.setEntreeNom(e.getDesignation());
			prs.setUnite(e.getUnite());
			prs.setIdEntree(e.getQualification().getId());
			prs.setType(Entree.OUVRIER);
		} else if (e.getType().equals(Entree.ARTICLE)) {
			prs.setEntreeNom(e.getDesignation());
			prs.setUnite(e.getUnite());
			prs.setIdEntree(e.getArticle().getId());
			prs.setType(Entree.ARTICLE);

		}

	}

}
