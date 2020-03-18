package org.amenal.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.amenal.entities.Ouvrier;
import org.amenal.entities.Projet;
import org.amenal.rest.commande.OuvrierCommande;
import org.amenal.rest.representation.OuvrierPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface OuvrierMapper {
	
	@Mapping(target="qualification" , source="e.qualification.code" )
	@Mapping(target="idProjets" , source="e.projets" , qualifiedByName="Projets_to_projetsId")
	OuvrierPresentation toRepresentation(Ouvrier e);
	
	@Mapping(target="qualification.code" , source="e.qualification" )
	Ouvrier toEntity(OuvrierCommande e);
	
	
	
	@Named("Projets_to_projetsId")
	default  List<Integer> Projets_to_projetsId(List<Projet> projets) {
		if(projets.isEmpty()) {
			return new ArrayList<Integer>();
		}
		return projets.stream().map(p -> p.getId()).collect(Collectors.toList());
		
	}

}
