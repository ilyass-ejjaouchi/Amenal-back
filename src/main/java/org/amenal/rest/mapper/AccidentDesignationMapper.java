package org.amenal.rest.mapper;

import org.amenal.entities.designations.AccidentDesignation;
import org.amenal.entities.designations.DocDesignation;
import org.amenal.rest.representation.AccidentDesignationPresentation;
import org.amenal.rest.representation.DocDesignationRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccidentDesignationMapper {
	
	AccidentDesignationPresentation toRepresentation(AccidentDesignation o);


}
