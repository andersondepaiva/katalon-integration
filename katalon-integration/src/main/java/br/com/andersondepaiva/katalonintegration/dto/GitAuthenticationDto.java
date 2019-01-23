package br.com.andersondepaiva.katalonintegration.dto;

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
public class GitAuthenticationDto extends BaseDto {
	private String uri;
	private String user;
	private String password;
	private String branch;
}
