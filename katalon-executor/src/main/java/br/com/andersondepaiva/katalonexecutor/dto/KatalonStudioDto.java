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
public class KatalonStudioDto extends BaseDto {
	private String path;
	private String workPath;
}
