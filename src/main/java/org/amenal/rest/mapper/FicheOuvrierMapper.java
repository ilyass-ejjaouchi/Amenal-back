package org.amenal.rest.mapper;

import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OuvrierDesignationMapper.class)
public interface FicheOuvrierMapper {

	@Mapping(source = "e.ouvrierDesignation", target = "designations")
	FichePresentation toRepresentation(OuvrierFiche e);
}
