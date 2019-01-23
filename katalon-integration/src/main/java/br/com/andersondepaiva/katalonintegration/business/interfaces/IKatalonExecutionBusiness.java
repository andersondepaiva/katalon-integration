package br.com.andersondepaiva.katalonintegration.business.interfaces;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonintegration.model.KatalonExecution;

public interface IKatalonExecutionBusiness extends IBusiness<KatalonExecution, String, KatalonExecutionDto> {
	void scheduleExecutionTest(KatalonExecutionDto dto) throws IOException, ReflectiveOperationException;

	Resource getLogFileAsResource(String idKatalonExecution);

	Resource getReportTestAsResource(String idKatalonExecution);

	Page<KatalonExecutionDto> getAll(String projectId, String requestedById, Pageable pageable);
}
