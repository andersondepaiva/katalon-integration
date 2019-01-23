package br.com.andersondepaiva.pessoa.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.andersondepaiva.pessoa.business.interfaces.IUsuarioBusiness;
import br.com.andersondepaiva.pessoa.dto.UsuarioDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioFiltroDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioPassDto;
import br.com.andersondepaiva.pessoa.dto.UsuarioWithAuthDto;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/usuario")
@Api
public class UsuarioController {

	@Autowired
	private IUsuarioBusiness usuarioBusiness;

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<UsuarioDto> Post(@Valid @RequestBody UsuarioWithAuthDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		UsuarioDto retorno = usuarioBusiness.saveUser(dto);
		return new ResponseEntity<UsuarioDto>(retorno, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = { "application/json" })
	public ResponseEntity<UsuarioDto> Put(@Valid @RequestBody UsuarioWithAuthDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		UsuarioDto retorno = usuarioBusiness.saveUser(dto);
		return new ResponseEntity<UsuarioDto>(retorno, HttpStatus.OK);
	}

	@RequestMapping(value = "/password", method = RequestMethod.PUT, consumes = { "application/json" })
	public ResponseEntity<Void> Put(@Valid @RequestBody UsuarioPassDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		usuarioBusiness.updatePassword(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/profile", method = RequestMethod.PUT, consumes = { "application/json" })
	public ResponseEntity<UsuarioDto> PutProfile(@Valid @RequestBody UsuarioDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		return new ResponseEntity<UsuarioDto>(usuarioBusiness.updateProfile(dto), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<UsuarioWithAuthDto> GetById(@PathVariable("id") String id) {
		Optional<UsuarioWithAuthDto> retorno = usuarioBusiness.findById(id);
		return retorno.isPresent() ? new ResponseEntity<UsuarioWithAuthDto>(retorno.get(), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public Page<UsuarioDto> GetAll(UsuarioFiltroDto filtro, Pageable pageable) {
		return usuarioBusiness.getAllWithoutAuthentication(filtro, pageable);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { "application/json" })
	public ResponseEntity<Void> DeleteById(@PathVariable("id") String id) {
		usuarioBusiness.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
