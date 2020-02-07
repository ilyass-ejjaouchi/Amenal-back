package org.amenal.rest.mapper;

import org.amenal.entities.Ouvrier;
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
		
		return qualification.getCode();
		
	}

}
