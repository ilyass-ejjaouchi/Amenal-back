package org.amenal.rest.mapper;

import org.amenal.entities.Fournisseur;

import org.amenal.rest.commande.FournisseurCommande;
import org.amenal.rest.representation.FournisseurPresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MaterielMapper.class)
public interface FournisseurMapper {

	FournisseurPresentation toRepresentation(Fournisseur e);

	Fournisseur toEntity(FournisseurCommande e);

}
