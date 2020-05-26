package org.amenal.entities.fiches;

import org.amenal.exception.BadRequestException;

public enum FicheTypeEnum {

	LOC("LOCATION"), MOO("OUVRIER"), RCP("RECEPTION"), STK("STOCK"), BSN("BESOIN"), LVR("LIVRAISON"), DOC("DOCUMENT")
	,ACC("ACCIDENT") , VST("VISITEUR"),ACT("ACTIVITE");
	private String code;

	private FicheTypeEnum(String code) {
		this.code = code;
	}

	public static FicheTypeEnum fromCode(String code) {
		switch (code) {
		case "LOCATION":
			return FicheTypeEnum.LOC;
		case "OUVRIER":
			return FicheTypeEnum.MOO;
		case "RECEPTION":
			return FicheTypeEnum.RCP;
		case "STOCK":
			return FicheTypeEnum.STK;
		case "BESOIN":
			return FicheTypeEnum.BSN;
		case "LIVRAISON":
			return FicheTypeEnum.LVR;
		case "DOCUMENT":
			return FicheTypeEnum.DOC;
		case "ACCIDENT":
			return FicheTypeEnum.ACC;
		case "VISITEUR":
			return FicheTypeEnum.VST;
		case "ACTIVITE":
			return FicheTypeEnum.ACT;
		default:
			throw new BadRequestException("l' accronyme [" + code + "] n' est pas supporter.");
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return code;
	}

}
