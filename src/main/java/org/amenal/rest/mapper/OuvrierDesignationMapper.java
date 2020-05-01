package org.amenal.rest.mapper;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.designations.OuvrierDesignation;
import org.amenal.entities.fiches.OuvrierFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.OuvrierDesignationCommande;
import org.amenal.rest.representation.OuvrierDesignationPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OuvrierDesignationMapper {

	
	@Mapping(target = "fiche", source = "e.idFiche" ,qualifiedByName="toFiche" )
	@Mapping(target = "ouvrier", source = "e.idOuvrier" ,qualifiedByName="toOuvrier" )
	OuvrierDesignation toEntity(OuvrierDesignationCommande e);
	
	@Mapping(target="qualification" , source="e.qualification" )
	@Mapping(target = "idOuvrier", source = "e.ouvrier.id" )
	OuvrierDesignationPresentation toRepresentation(OuvrierDesignation e);
	
	
	
	
	
	
	@Named("toFiche")
	public default OuvrierFiche toFiche(Integer idFiche) {
		if(idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		OuvrierFiche ouvFiche = new OuvrierFiche();
		ouvFiche.setId(idFiche);
		return ouvFiche;
	}

	@Named("toOuvrier")
	public default Ouvrier toOuvrier(Integer idOuvrier) {
		if(idOuvrier == null)
			return null;
		Ouvrier ouvrier = new Ouvrier();
		ouvrier.setId(idOuvrier);
		return ouvrier;
	}

}
