package org.amenal.rest.mapper;

import org.amenal.dao.pojo.FournisseurArticleBsn;
import org.amenal.entities.Fournisseur;
import org.amenal.rest.representation.FournisseurArticlePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArticleMapper.class , CategorieArticleMapper.class } )
public interface FournisseurArticleMapper {
	
	
	FournisseurArticlePresentation toRepresentation(FournisseurArticleBsn e);
	
	@Mapping(source="e.article" , target="article")
	FournisseurArticlePresentation toRepresentation(Fournisseur e);


}
