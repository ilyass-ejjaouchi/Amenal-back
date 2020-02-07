package org.amenal.entities.fiches;

public enum FicheTypeEnum {

	LOC("LOC"), MOO("MOO");

	private String code;

	private FicheTypeEnum(String code) {
		this.code = code;
	}

	public static FicheTypeEnum fromCode(String code) {
		switch (code) {
		case "LOC":
			return FicheTypeEnum.LOC;
		case "MOO":
			return FicheTypeEnum.MOO;
		default:
			return null;
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
