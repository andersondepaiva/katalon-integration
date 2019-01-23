package br.com.andersondepaiva.katalonexecutor.dto;

import br.com.andersondepaiva.core.dto.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ResultCmdKatalonDto extends BaseDto {

	private int exitCode;
	private String pathReport;
}
