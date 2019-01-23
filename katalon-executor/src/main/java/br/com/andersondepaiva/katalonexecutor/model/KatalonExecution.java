package br.com.andersondepaiva.katalonexecutor.model;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.andersondepaiva.core.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Document(collection = "katalon-executions")
public class KatalonExecution extends BaseModel {

	private Project project;
	private Profile profile;
	private TestSuite testSuite;
	private StatusExecution statusExecution;
	private String traceExecution;
	private String browser;
	private String pathLogFile;
	private String pathReportTest;
}
