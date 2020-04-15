package org.amenal.rest.mapper;

import org.amenal.entities.Article;
import org.amenal.entities.Destination;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.LivraisonDesignation;
import org.amenal.entities.fiches.LivraisonFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.LivraisonDesignationCommande;
import org.amenal.rest.representation.LivraisonDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LivraisonDesignationMapper {

	@Mapping(target = "articleId", source = "e.articleLvr.id")
	@Mapping(target = "destinationId", source = "e.destination.id")
	LivraisonDesignationPresentation toRepresentation(LivraisonDesignation e);

	@Mapping(source = "e.idMateriel", target = "articleLvr", qualifiedByName = "toArticle")
	@Mapping(target = "livraisonFiche", source = "e.idFiche", qualifiedByName = "toFiche")
	@Mapping(target = "destination", source = "e.destinationId", qualifiedByName = "toDestination")
	LivraisonDesignation toEntity(LivraisonDesignationCommande e);

	@Named("toDestination")
	public default Destination toDestination(Integer destinationId) {
		if (destinationId == null)
			throw new BadRequestException("Vous devez specifiez une destination valide!");
		Destination dst = new Destination();
		dst.setId(destinationId);
		return dst;
	}

	@Named("toArticle")
	public default Article toArticle(Integer idArticle) {
		if (idArticle == null)
			throw new BadRequestException("Vous devez specifiez un article valide!");
		Article article = new Article();
		article.setId(idArticle);
		return article;
	}

	@Named("toFiche")
	public default LivraisonFiche toFiche(Integer idFiche) {
		if (idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		LivraisonFiche locFiche = new LivraisonFiche();
		locFiche.setId(idFiche);
		return locFiche;
	}

	/*
	 * @Named("toFournisseur") public default Fournisseur toFournisseur(Integer
	 * idFournisseur) { if (idFournisseur == null) throw new
	 * BadRequestException("Vous devez specifiez un fournisseur valide!");
	 * Fournisseur fr = new Fournisseur(); fr.setId(idFournisseur); return fr; }
	 */
}
