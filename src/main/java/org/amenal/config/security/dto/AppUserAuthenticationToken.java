package org.amenal.config.security.dto;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AppUserAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;
	private boolean isRoot;

	public AppUserAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities, boolean isRoot) {
		super(principal, credentials, authorities);
		// TODO Auto-generated constructor stub
		this.isRoot = isRoot;

	}

	/**
	 * 
	 */

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

}
