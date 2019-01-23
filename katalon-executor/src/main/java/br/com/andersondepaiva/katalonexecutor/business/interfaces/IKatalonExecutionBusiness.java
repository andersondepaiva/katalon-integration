package br.com.andersondepaiva.katalonexecutor.business.interfaces;

import java.io.IOException;

import br.com.andersondepaiva.core.business.IBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonexecutor.model.KatalonExecution;

public interface IKatalonExecutionBusiness extends IBusiness<KatalonExecution, String, KatalonExecutionDto> {

	boolean executeTests(KatalonExecutionDto dto) throws IOException, InterruptedException, ReflectiveOperationException;
}
