package org.amenal.dao.EnumConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.amenal.entities.QualificationOuvrierEnum;

@Converter(autoApply = true)
public class QualificationOuvrierEnumConverter implements AttributeConverter<QualificationOuvrierEnum, String>  {

	@Override
	public String convertToDatabaseColumn(QualificationOuvrierEnum qualification) {
		return qualification.getCode();
	}

	@Override
	public QualificationOuvrierEnum convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		return QualificationOuvrierEnum.fromCode(dbData);
	}

	
}
