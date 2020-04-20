package org.amenal.rest.mapper;

import org.amenal.entities.fiches.AccidentFiche;
import org.amenal.entities.fiches.DocFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AccidentDesignationMapper.class)
public interface FicheAccidentMapper {
	
	@Mapping(source = "e.accDesignations", target = "designations")
	FichePresentation toRepresentation(AccidentFiche e);

}