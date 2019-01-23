package br.com.andersondepaiva.katalonintegration.model;

import org.springframework.data.mongodb.core.mapping.Document;

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
public class Project extends CustomFile {

	private SourceType sourceType;
	private GitAuthentication gitAuthentication;
}
