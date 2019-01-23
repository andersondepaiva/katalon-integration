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
@Document(collection = "projects")
public class GitAuthentication extends BaseModel {

	private String uri;
	private String user;
	private String password;
	private String branch;
}
