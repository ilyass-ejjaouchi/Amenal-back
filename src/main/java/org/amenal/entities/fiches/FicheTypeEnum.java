package org.amenal.entities.fiches;

import org.amenal.exception.BadRequestException;

public enum FicheTypeEnum {

	LOC("LOCATION"), MOO("OUVRIER");

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
		default:
			throw new BadRequestException("l' accronyme [" + code
                    + "] n' est pas supporter.");
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
