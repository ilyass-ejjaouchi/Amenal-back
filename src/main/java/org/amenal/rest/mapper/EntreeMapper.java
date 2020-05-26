package org.amenal.rest.mapper;

import org.amenal.entities.Entree;
import org.amenal.rest.representation.EntreePresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntreeMapper {

	EntreePresentation toRepresentation(Entree e);

}
