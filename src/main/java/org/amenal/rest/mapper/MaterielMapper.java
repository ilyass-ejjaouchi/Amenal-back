package org.amenal.rest.mapper;

import org.amenal.entities.Materiel;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.representation.MaterielPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterielMapper {
	
	MaterielPresentation toRepresentation(Materiel e);
	
	Materiel toEntity(MaterielCommande e);
}
