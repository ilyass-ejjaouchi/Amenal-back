package org.amenal.rest.mapper;



import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.LocationDesignation;
import org.amenal.entities.fiches.LocationFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.LocationDesignationCommande;
import org.amenal.rest.representation.LocationDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface LocationDesignationMapper {
	
	LocationDesignationPresentation toRepresentation(LocationDesignation o);
	
	@Mapping(target = "fiche", source = "e.idFiche" ,qualifiedByName="toFiche" )
	@Mapping(target = "materiel", source = "e.idMateriel" ,qualifiedByName="toMateriel" )
	@Mapping(target = "fournisseur", source = "e.fournisseurId" ,qualifiedByName="toFournisseur" )
	LocationDesignation toEntity(LocationDesignationCommande e);

	
	@Named("toFiche")
	public default LocationFiche toFiche(Integer idFiche) {
		if(idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		LocationFiche locFiche = new LocationFiche();
		locFiche.setId(idFiche);
		return locFiche;
	}
	
	@Named("toMateriel")
	public default Article toArticle(Integer idArticle) {
		if(idArticle == null)
			throw new BadRequestException("Vous devez specifiez un article valide!");
		Article article = new Article();
		article.setId(idArticle);
		return article;
	}
	
	@Named("toFournisseur")
	public default Fournisseur toFournisseur(Integer idFournisseur) {
		if(idFournisseur == null)
			throw new BadRequestException("Vous devez specifiez un fournisseur valide!");
		Fournisseur fr = new Fournisseur();
		fr.setId(idFournisseur);
		return fr;
	}
	
}
