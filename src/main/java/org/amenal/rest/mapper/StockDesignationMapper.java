package org.amenal.rest.mapper;

import org.amenal.entities.designations.StockDesignation;
import org.amenal.rest.representation.StockDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface StockDesignationMapper {

	@Mapping(target = "articleId", source = "e.article.id"  )
	StockDesignationPresentation toRepresentation(StockDesignation e);
	
	
}
