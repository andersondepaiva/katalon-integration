package br.com.andersondepaiva.katalonintegration.controller;

import java.security.NoSuchAlgorithmException;
import java.util.List;
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

import br.com.andersondepaiva.katalonintegration.business.interfaces.IGitBusiness;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IProjectBusiness;
import br.com.andersondepaiva.katalonintegration.dto.ProjectBaseDto;
import br.com.andersondepaiva.katalonintegration.dto.ProjectDto;
import br.com.andersondepaiva.katalonintegration.dto.ProjectItemsDto;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/project")
@Api
public class ProjectController {

	@Autowired
	private IProjectBusiness projectBusiness;

	@Autowired
	private IGitBusiness gitBusiness;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<ProjectDto> GetById(@PathVariable("id") String id) {
		Optional<ProjectDto> retorno = projectBusiness.findById(id);
		return retorno.isPresent() ? new ResponseEntity<ProjectDto>(retorno.get(), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<ProjectItemsDto> GetItemsById(@PathVariable("id") String id) {
		ProjectItemsDto retorno = projectBusiness.getItemsProject(id);
		return retorno != null ? new ResponseEntity<ProjectItemsDto>(retorno, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/branches", method = RequestMethod.GET, produces = { "application/json" })
	public ResponseEntity<List<String>> GetBranches(String uri, String user, String password) {
		List<String> retorno = gitBusiness.getAllBranches(uri, user, password);
		return new ResponseEntity<List<String>>(retorno, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { "application/json" })
	public ResponseEntity<Void> DeleteById(@PathVariable("id") String id) {
		projectBusiness.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public Page<ProjectBaseDto> GetAll(ProjectDto filter, Pageable filtro) {
		return projectBusiness.getAllWithoutGitAuthentication(filter, filtro);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<ProjectBaseDto> Post(@Valid @RequestBody ProjectDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		ProjectBaseDto retorno = projectBusiness.customSave(dto);
		return new ResponseEntity<ProjectBaseDto>(retorno, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = { "application/json" })
	public ResponseEntity<ProjectBaseDto> Put(@Valid @RequestBody ProjectDto dto)
			throws ReflectiveOperationException, NoSuchAlgorithmException {
		ProjectBaseDto retorno = projectBusiness.customSave(dto);
		return new ResponseEntity<ProjectBaseDto>(retorno, HttpStatus.OK);
	}

}
