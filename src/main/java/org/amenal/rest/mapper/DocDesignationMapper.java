package org.amenal.rest.mapper;

import org.amenal.entities.designations.DocDesignation;
import org.amenal.rest.representation.DocDesignationRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocDesignationMapper {
	
	DocDesignationRepresentation toRepresentation(DocDesignation o);

	

}
