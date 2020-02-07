package org.amenal.rest.mapper;

import java.util.ArrayList;
import java.util.List;

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
	@Mapping(target="fichierTypes" , source="e.fichierTypes" , qualifiedByName="enum_to_string")
	ProjetPresentation toRepresentation(Projet e);
	
	@Mapping(target = "titre", source = "e.titre")
	Projet toEntity(ProjetCommande e);
	
	
	@Named("enum_to_string")
	default  List<String> fromEnumToString(List<FicheTypeEnum> ficheType) {
		
		List<String> fiche_Type_String = new ArrayList<String>();
		
		ficheType.forEach(f->{
			fiche_Type_String.add(f.toString());
		});
		
		return fiche_Type_String;
		
	}

}
