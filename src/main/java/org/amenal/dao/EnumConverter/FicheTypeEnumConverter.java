package org.amenal.dao.EnumConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.amenal.entities.fiches.FicheTypeEnum;

@Converter(autoApply = true)
public class FicheTypeEnumConverter implements AttributeConverter<FicheTypeEnum, String>  {

	@Override
	public String convertToDatabaseColumn(FicheTypeEnum fichierType) {
		return fichierType.getCode();
	}

	@Override
	public FicheTypeEnum convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		return FicheTypeEnum.fromCode(dbData);
	}

	
}
