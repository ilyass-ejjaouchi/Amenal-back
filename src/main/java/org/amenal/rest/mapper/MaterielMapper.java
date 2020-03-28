package org.amenal.rest.mapper;

import org.amenal.entities.Article;
import org.amenal.rest.commande.MaterielCommande;
import org.amenal.rest.representation.MaterielPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterielMapper {
	
	@Mapping(source = "e.unite.unite", target = "unite")
	MaterielPresentation toRepresentation(Article e);
	
	@Mapping(source = "e.unite", target = "unite.unite")
	Article toEntity(MaterielCommande e);
}
