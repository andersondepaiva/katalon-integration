package br.com.andersondepaiva.auth.security;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
	USER, ADMIN;

	public static String[] names() {
		String[] names = new String[values().length];
		for (int index = 0; index < values().length; index++) {
			names[index] = values()[index].name();
		}

		return names;
	}

	public String getAuthority() {
		return this.name();
	}
}
