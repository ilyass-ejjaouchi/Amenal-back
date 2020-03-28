package org.amenal.rest.mapper;

import org.amenal.entities.Article;
import org.amenal.entities.Fournisseur;
import org.amenal.entities.designations.ReceptionDesignation;
import org.amenal.entities.fiches.ReceptionFiche;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.ReceptionDesignationCommande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")

public interface ReceptionDesignationMapper {
	
	
	@Mapping(target = "receptionfiche", source = "e.idFiche" ,qualifiedByName="toFiche" )

	ReceptionDesignation toEntity(ReceptionDesignationCommande e);

	
	@Named("toFiche")
	public default ReceptionFiche toFiche(Integer idFiche) {
		if(idFiche == null)
			throw new BadRequestException("Vous devez specifiez la fiche");
		ReceptionFiche recFiche = new ReceptionFiche();
		recFiche.setId(idFiche);
		return recFiche;
	}
	
	
	
	

}
