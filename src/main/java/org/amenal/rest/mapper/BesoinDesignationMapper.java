package org.amenal.rest.mapper;

import org.amenal.entities.designations.BesoinDesignation;
import org.amenal.entities.fiches.BesoinFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.BesoinDesignationCommande;
import org.amenal.rest.representation.BesoinDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BesoinDesignationMapper {

	BesoinDesignationPresentation toRepresentation(BesoinDesignation o);

	@Mapping(target = "fiche", source = "e.idFiche", qualifiedByName = "toFiche")
	BesoinDesignation toEntity(BesoinDesignationCommande e);

	@Named("toFiche")
	public default BesoinFiche toFiche(Integer idFiche) {
		if (idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		BesoinFiche locFiche = new BesoinFiche();
		locFiche.setId(idFiche);
		return locFiche;
	}
	
	

}
