package org.amenal.rest.commande;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
	private String username;
	private String password;
	private Integer projetId ;
	private String role;
	
	 
}