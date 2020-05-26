package org.amenal.rest.mapper;

import org.amenal.entities.Lot;

import org.amenal.rest.representation.LotPresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SousLotMapper.class)
public interface LotMapper {

	LotPresentation toRepresentation(Lot e);

}
