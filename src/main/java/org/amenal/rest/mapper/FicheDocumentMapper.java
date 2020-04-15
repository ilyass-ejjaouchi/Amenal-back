package org.amenal.rest.mapper;

import org.amenal.entities.fiches.DocFiche;
import org.amenal.rest.representation.FichePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DocDesignationMapper.class)
public interface FicheDocumentMapper {
	
	@Mapping(source = "e.docDesignations", target = "designations")
	FichePresentation toRepresentation(DocFiche e);

}
