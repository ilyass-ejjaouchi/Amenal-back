package org.amenal.rest.mapper;

import org.amenal.entities.fiches.ActiviteFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ActiviteDesignationMapper.class)
public interface FicheActiviteMapper {
	
	@Mapping(source = "e.activiteDesignations", target = "designations")
	FichePresentation toRepresentation(ActiviteFiche e);

}