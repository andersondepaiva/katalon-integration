package br.com.andersondepaiva.katalonintegration.model;

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
public class CustomFile extends BaseModel {

	private String name;
	private String path;
}
