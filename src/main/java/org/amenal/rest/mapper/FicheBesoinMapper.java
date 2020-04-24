package org.amenal.rest.mapper;

import org.amenal.entities.fiches.BesoinFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BesoinDesignationMapper.class)

public interface FicheBesoinMapper {

	@Mapping(source = "e.besoinDesignations", target = "designations")
	FichePresentation toRepresentation(BesoinFiche e);
}
