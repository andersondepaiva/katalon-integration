package br.com.andersondepaiva.auth.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.auth.business.interfaces.IAuthBusiness;
import br.com.andersondepaiva.auth.dto.UsuarioDto;
import br.com.andersondepaiva.auth.model.Usuario;
import br.com.andersondepaiva.auth.repository.IUsuarioRepository;
import br.com.andersondepaiva.core.business.Business;

@Service
public class AuthBusiness extends Business<Usuario, String, UsuarioDto> implements IAuthBusiness, UserDetailsService {

	@Autowired
	public AuthBusiness(IUsuarioRepository baseRepository) {
		super(baseRepository);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Usuario> usuarios = ((IUsuarioRepository) baseRepository).findByLoginAndExcluidoFalse(username);

		if (usuarios.isEmpty() || usuarios.size() > 1) {
			throw new UsernameNotFoundException("Invalid Authentication.");
		}

		UsuarioDto usuarioDto = modelMapper.map(usuarios.get(0), UsuarioDto.class);

		return usuarioDto;
	}
}
