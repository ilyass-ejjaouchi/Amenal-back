package org.amenal.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class QualificationOuvrier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	private String code;

	@OneToMany(mappedBy = "qualification")
	private List<Ouvrier> ouvriers;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.trim().toUpperCase();
	}

}
