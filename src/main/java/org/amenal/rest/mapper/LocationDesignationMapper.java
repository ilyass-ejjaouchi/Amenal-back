package org.amenal.rest.mapper;



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
	
	@Mapping(target = "locationFiche", source = "e.idFiche" ,qualifiedByName="toFiche" )
	LocationDesignation toEntity(LocationDesignationCommande e);

	
	@Named("toFiche")
	public default LocationFiche toFiche(Integer idFiche) {
		if(idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		LocationFiche locFiche = new LocationFiche();
		locFiche.setId(idFiche);
		return locFiche;
	}
	
}
