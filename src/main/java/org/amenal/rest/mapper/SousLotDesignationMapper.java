package org.amenal.rest.mapper;

import org.amenal.entities.designations.SousLotDesignation;
import org.amenal.rest.representation.SousLotDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = EntreeDesignationMapper.class)
public interface SousLotDesignationMapper {

	@Mapping(source = "e.entreeDesignations", target = "entreeDesignationPresentations")
	@Mapping(source = "e.sousLot.id", target = "slotid")
	SousLotDesignationPresentation toRepresentation(SousLotDesignation e);

}
