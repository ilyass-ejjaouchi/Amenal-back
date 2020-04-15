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

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Unite {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String unite;
	
	private Boolean showUnite;
	
    @OneToMany(mappedBy="unite")
	List<Article> articles;
    
    public String getUnite() {
		return unite;
	}

	public void setUnite(String unite) {
		this.unite = unite.trim().toUpperCase();
	}
    
}
