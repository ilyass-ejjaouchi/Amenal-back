package org.amenal.rest.mapper;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.entities.QualificationOuvrierEnum;
import org.amenal.exception.BadRequestException;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.representation.OuvrierPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface OuvrierMapper {
	
	@Mapping(target="qualification" , source="e.qualification" , qualifiedByName="enum_to_string")
	@Mapping(target="idProjets" , source="e.projets" , qualifiedByName="Projets_to_projetsId")
	OuvrierPresentation toRepresentation(Ouvrier e);
	
	@Mapping(target="qualification" , source="e.qualification" , qualifiedByName="string_to_enum")
	Ouvrier toEntity(OuvrierCommande e);
	
	
	@Named("string_to_enum")
	default  QualificationOuvrierEnum fromEnumToString(String code) {
		
		if(code ==null)
			throw new BadRequestException("Qualification et un champs obligatoire!");
		return QualificationOuvrierEnum.fromCode(code) ;
		
	}
	
	@Named("enum_to_string")
	default  String fromEnumToString(QualificationOuvrierEnum qualification) {
		if(qualification == null)
			return null;
		
		
		return qualification.getCode();
		
	}
	@Named("Projets_to_projetsId")
	default  List<Integer> Projets_to_projetsId(List<Projet> projets) {
		System.out.println("oppooppopoooooooooooooooooooooooooooooooooooo");
		if(projets.isEmpty()) {
			return new ArrayList<Integer>();
		}
		return projets.stream().map(p -> p.getId()).collect(Collectors.toList());
		
	}

}
