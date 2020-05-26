package org.amenal.rest.mapper;

import java.util.ArrayList;
import java.util.List;

import org.amenal.entities.Entree;
import org.amenal.entities.SousLot;
import org.amenal.rest.representation.EntreePresentation;
import org.amenal.rest.representation.SousLotPresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = EntreeMapper.class)
public interface SousLotMapper {

	@Mapping(target = "entrees", source = "e.entrees", qualifiedByName = "entree")
	@Mapping(target = "unite", source = "e.unite.unite")
	SousLotPresentation toRepresentation(SousLot e);

	@Named("entree")
	default List<EntreePresentation> fromEnumToString(List<Entree> entrees) {

		List<EntreePresentation> entreePrs = new ArrayList<EntreePresentation>();

		for (Entree e : entrees) {

			EntreePresentation prs = new EntreePresentation();
			if (e.getType().equals("ouv")) {

				prs.setId(e.getId());
				prs.setIdEntree(e.getQualification().getId());
				prs.setEntreeNom(e.getQualification().getCode());
				prs.setUnite("H");
				prs.setType("ouv");
				prs.setQuantiteEstimer(e.getQuantiteEstimer());

			} else if (e.getType().equals("art")) {

				prs.setId(e.getId());
				prs.setEntreeNom(e.getArticle().getDesignation());
				prs.setIdEntree(e.getArticle().getId());
				prs.setUnite(e.getArticle().getUnite().getUnite());
				prs.setType("art");
				prs.setQuantiteEstimer(e.getQuantiteEstimer());

			}

			entreePrs.add(prs);
		}

		return entreePrs;

	}

}
