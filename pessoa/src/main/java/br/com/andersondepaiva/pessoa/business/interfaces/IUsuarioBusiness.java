package br.com.andersondepaiva.pessoa.business.interfaces;

import java.security.NoSuchAlgorithmException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.pessoa.dto.UsuarioDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioFiltroDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioPassDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioWithAuthDto;
import br.com.andersondepaiva.pessoa.model.Usuario;

@Service
public interface IUsuarioBusiness extends IBusiness<Usuario, String, UsuarioWithAuthDto> {
	
	UsuarioDto saveUser(UsuarioWithAuthDto dto) throws ReflectiveOperationException, NoSuchAlgorithmException;
	Page<UsuarioDto> getAllWithoutAuthentication(UsuarioFiltroDto filtros, Pageable pageable);
	void updatePassword(UsuarioPassDto dto);
	UsuarioDto updateProfile(UsuarioDto dto);
}