package br.com.andersondepaiva.auth.dto;

import java.util.Arrays;
import java.util.Collection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.andersondepaiva.auth.security.Perfil;
import br.com.andersondepaiva.core.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class UsuarioDto extends BaseDto implements UserDetails, Authentication {

	private String senha;
	private Perfil tipoUsuario;
	private String login;
	private boolean authenticated = true;

	@NotEmpty(message = "Senha é obrigatória")
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@NotNull(message = "Tipo do Usuário é obrigatório")
	public Perfil getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(Perfil tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@NotEmpty(message = "Login é obrigatório")
	@Email(message = "E-mail inválido")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.login;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return !this.getExcluido();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.login;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return this.getPassword();
	}

	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return this.getLogin();
	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return this.authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		this.authenticated = isAuthenticated;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Arrays.asList(this.tipoUsuario);
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return !this.getExcluido();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !this.getExcluido();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return !this.getExcluido();
	}

}
