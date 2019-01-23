package br.com.andersondepaiva.katalonintegration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class KatalonExecutionFilter {

	private String project;
	private String requestedBy;
}
