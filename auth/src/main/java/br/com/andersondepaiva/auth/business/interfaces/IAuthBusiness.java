package br.com.andersondepaiva.auth.business.interfaces;

import org.springframework.stereotype.Service;

import br.com.andersondepaiva.auth.dto.UsuarioDto;
import br.com.andersondepaiva.auth.model.Usuario;
import br.com.andersondepaiva.core.business.IBusiness;

@Service
public interface IAuthBusiness extends IBusiness<Usuario, String, UsuarioDto> {

}