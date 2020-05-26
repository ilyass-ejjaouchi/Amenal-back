package org.amenal.rest.mapper;

import org.amenal.entities.designations.ActiviteDesignation;

import org.amenal.rest.representation.ActiviteDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring" , uses=SousLotDesignationMapper.class)
public interface ActiviteDesignationMapper {

	@Mapping(target = "sousLotDesignationPresentations", source = "e.sousLotDesignations")
	@Mapping(target = "lot", source = "e.designation" )
	ActiviteDesignationPresentation toRepresentation(ActiviteDesignation e);
	
	

}
