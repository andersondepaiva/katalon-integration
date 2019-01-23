package br.com.andersondepaiva.katalonintegration.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonExecutionBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionFilter;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/katalon-execution")
@Api
public class KatalonExecutionController {

	@Autowired
	private IKatalonExecutionBusiness katalonBusiness;

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
	public ResponseEntity<Void> scheduleExecutionTest(@RequestBody KatalonExecutionDto dto)
			throws IOException, ReflectiveOperationException {
		katalonBusiness.scheduleExecutionTest(dto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public Page<KatalonExecutionDto> getAll(KatalonExecutionFilter katalonFilter, Pageable filtro) {
		return katalonBusiness.getAll(katalonFilter.getProject(), katalonFilter.getRequestedBy(), filtro);
	}

	@RequestMapping(value = "/report-test/{id}", method = RequestMethod.GET)
	public void GetItemsById(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
		Resource file = katalonBusiness.getReportTestAsResource(id);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("file-name", file.getFilename());
		response.setStatus(200);
		response.getOutputStream().write(IOUtils.toByteArray(file.getInputStream()));
	}

	@RequestMapping(value = "/log/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Resource> GetItemsById(@PathVariable("id") String id) throws IOException {
		Resource file = katalonBusiness.getLogFileAsResource(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
