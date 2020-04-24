package org.amenal.rest.mapper;

import org.amenal.entities.Visiteur;

import org.amenal.rest.commande.VisiteurCommande;
import org.amenal.rest.representation.VisiteurPresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface VisiteurMapper {

	Visiteur toEntity(VisiteurCommande e);

	VisiteurPresentation toRepresentation(Visiteur o);

}
