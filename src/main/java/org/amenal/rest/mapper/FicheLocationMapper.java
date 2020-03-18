package org.amenal.rest.mapper;

import org.amenal.entities.fiches.LocationFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LocationDesignationMapper.class)
public interface FicheLocationMapper {

	@Mapping(source = "e.locationDesignations", target = "designations")
	FichePresentation toRepresentation(LocationFiche e);
}
