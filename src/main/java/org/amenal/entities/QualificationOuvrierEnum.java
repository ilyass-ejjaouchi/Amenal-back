package org.amenal.entities;


public enum QualificationOuvrierEnum {
	
	BSR("Boiseur"), MAC("Macon");

	private String code;

	private QualificationOuvrierEnum(String code) {
		this.code = code;
	}

	public static QualificationOuvrierEnum fromCode(String code) {
		switch (code) {
		case "Boiseur":
			return QualificationOuvrierEnum.BSR;
		case "Macon":
			return QualificationOuvrierEnum.MAC;
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

}
