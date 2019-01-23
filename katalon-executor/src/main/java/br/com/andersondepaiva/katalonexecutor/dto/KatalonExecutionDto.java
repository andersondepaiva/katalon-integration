package br.com.andersondepaiva.katalonexecutor.dto;

import br.com.andersondepaiva.core.dto.BaseDto;
import br.com.andersondepaiva.katalonexecutor.model.StatusExecution;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class KatalonExecutionDto extends BaseDto {

	private ProjectDto project;
	private ProfileDto profile;
	private TestSuiteDto testSuite;
	private StatusExecution statusExecution;
	private String traceExecution;
	private String browser;
	private String pathLogFile;
	private String pathReportTest;
}
