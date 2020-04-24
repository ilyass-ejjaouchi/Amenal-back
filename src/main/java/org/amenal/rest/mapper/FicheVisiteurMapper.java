package org.amenal.rest.mapper;

import org.amenal.entities.fiches.VisiteurFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = VisiteurDesignationMapper.class)
public interface FicheVisiteurMapper {
	
	@Mapping(source = "e.visiteurDesignations", target = "designations")
	FichePresentation toRepresentation(VisiteurFiche e);


}
