package org.amenal.rest.mapper;

import org.amenal.entities.CategorieArticle;
import org.amenal.rest.commande.CategorieArticleCommande;
import org.amenal.rest.representation.CategorieArticlePresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ArticleMapper.class)
public interface CategorieArticleMapper {

	CategorieArticlePresentation toRepresentation(CategorieArticle e);

	CategorieArticle toEntity(CategorieArticleCommande e);

}
