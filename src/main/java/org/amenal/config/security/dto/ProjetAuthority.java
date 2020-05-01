package org.amenal.config.security.dto;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ProjetAuthority implements GrantedAuthority {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer projetId;

	private String role;

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return role;
	}

	public Integer getProjetId() {
		return projetId;
	}

	public void setProjetId(Integer projetId) {
		this.projetId = projetId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((projetId == null) ? 0 : projetId.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
	
	

}
