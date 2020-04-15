package org.amenal.rest.mapper;

import org.amenal.entities.designations.Stock;

import org.amenal.rest.representation.StockPresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" , uses=StockDesignationMapper.class)

public interface StockMapper {
	
	StockPresentation toRepresentation(Stock e);


}
