package org.amenal.rest.mapper;


import org.amenal.entities.Document;
import org.amenal.rest.representation.DocumentRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface DocumentMapper {
	DocumentRepresentation toRepresentation(Document e);


}
