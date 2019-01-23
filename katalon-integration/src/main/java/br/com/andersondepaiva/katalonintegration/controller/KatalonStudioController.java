package br.com.andersondepaiva.katalonintegration.controller;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonStudioBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonStudioDto;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/katalon-studio")
@Api
public class KatalonStudioController {

	@Autowired
	private IKatalonStudioBusiness katalonStudioBusiness;

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<KatalonStudioDto> GetAll(Pageable filtro) {
		Page<KatalonStudioDto> pageKatalonStudio = katalonStudioBusiness.getAll(filtro);
		return pageKatalonStudio == null || pageKatalonStudio.isEmpty()
				? new ResponseEntity<KatalonStudioDto>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<KatalonStudioDto>(katalonStudioBusiness.getAll(filtro).stream().findFirst().get(),
						HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<KatalonStudioDto> Post(@Valid @RequestBody KatalonStudioDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		KatalonStudioDto retorno = katalonStudioBusiness.save(dto);
		return new ResponseEntity<KatalonStudioDto>(retorno, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = { "application/json" })
	public ResponseEntity<KatalonStudioDto> Put(@Valid @RequestBody KatalonStudioDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		KatalonStudioDto retorno = katalonStudioBusiness.save(dto);
		return new ResponseEntity<KatalonStudioDto>(retorno, HttpStatus.OK);
	}

}
