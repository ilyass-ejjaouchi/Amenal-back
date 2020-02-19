package org.amenal.rest.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.amenal.entities.Projet;
import org.amenal.entities.fiches.FicheTypeEnum;
import org.amenal.rest.commande.ProjetCommande;
import org.amenal.rest.representation.ProjetPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProjetMapper {

	@Mapping(target = "titre", source = "e.titre")
	@Mapping(target = "fichierTypes", source = "e.fichierTypes", qualifiedByName = "enum_to_string")
	ProjetPresentation toRepresentation(Projet e);

	@Mapping(target = "titre", source = "e.titre")
	@Mapping(target = "fichierTypes", source = "e.fichierTypes", qualifiedByName = "string_to_enum")
	Projet toEntity(ProjetCommande e);

	@Named("string_to_enum")
	default List<FicheTypeEnum> fromStringToEnum(List<String> ficheTypes){
       return ficheTypes.stream().distinct().map(x->FicheTypeEnum.fromCode(x)).collect(Collectors.toList());
 
	}

	@Named("enum_to_string")
	default List<String> fromEnumToString(List<FicheTypeEnum> ficheTypes) {
		
		return ficheTypes.stream().distinct().map((x)->x.getCode()).collect(Collectors.toList());


	}

}
