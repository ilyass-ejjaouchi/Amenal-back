package org.amenal.rest.mapper;

import org.amenal.entities.designations.VisiteurDesignation;
import org.amenal.entities.fiches.VisiteurFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.VisiteurDesignationCommande;
import org.amenal.rest.representation.VisiteurDesignationRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface VisiteurDesignationMapper {
	
	@Mapping(target = "visiteurFiche", source = "e.idFiche" ,qualifiedByName="toFiche" )
	VisiteurDesignation toEntity(VisiteurDesignationCommande e);
	
	@Mapping(target = "visiteurId", source = "e.visiteur.id"  )
	VisiteurDesignationRepresentation toRepresentation(VisiteurDesignation e);

	
	@Named("toFiche")
	public default VisiteurFiche toFiche(Integer idFiche) {
		if(idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		VisiteurFiche vstFiche = new VisiteurFiche();
		vstFiche.setId(idFiche);
		return vstFiche;
	}
}
