package org.amenal.rest.mapper;

import org.amenal.entities.Article;
import org.amenal.rest.commande.ArticleCommande;
import org.amenal.rest.representation.ArticlePresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
	
	@Mapping(source = "e.unite.unite", target = "unite")
	ArticlePresentation toRepresentation(Article e);

	@Mapping(source = "e.categorieID", target = "categorie.id")
	@Mapping(source = "e.unite", target = "unite.unite")
	Article toEntity(ArticleCommande e);

}
