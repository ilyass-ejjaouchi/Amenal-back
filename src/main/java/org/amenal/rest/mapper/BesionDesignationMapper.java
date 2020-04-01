package org.amenal.rest.mapper;

import org.amenal.entities.Article;
import org.amenal.entities.designations.BesionDesignation;
import org.amenal.entities.fiches.BesionFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.BesionDesignationCommande;
import org.amenal.rest.representation.BesionDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BesionDesignationMapper {

	BesionDesignationPresentation toRepresentation(BesionDesignation o);

	/*@Mapping(target = "besionFiche", source = "e.idFiche", qualifiedByName = "toFiche")
	@Mapping(target = "article", source = "e.ArticleId", qualifiedByName = "toArticle")*/
	BesionDesignation toEntity(BesionDesignationCommande e);

	@Named("toFiche")
	public default BesionFiche toFiche(Integer idFiche) {
		if (idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		BesionFiche locFiche = new BesionFiche();
		locFiche.setId(idFiche);
		return locFiche;
	}
	
	@Named("toArticle")
	public default Article toArticle(Integer idArticle) {
		if(idArticle == null)
			throw new BadRequestException("Vous devez specifiez un article valide!");
		Article article = new Article();
		article.setId(idArticle);
		return article;
	}

}
