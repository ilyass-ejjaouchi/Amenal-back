package org.amenal.entities;

import org.amenal.exception.BadRequestException;

public enum QualificationOuvrierEnum {
	
	BSR("Boiseur"), MAC("Macon") , ING("Ingenieur");

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
		case "Ingenieur":
			return QualificationOuvrierEnum.ING;
		default:
			throw new BadRequestException("l' accronye [" + code
                    + "] n' est pas supporter.");
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
