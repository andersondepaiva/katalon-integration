package br.com.andersondepaiva.katalonintegration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ProjectDto extends ProjectBaseDto {
	private GitAuthenticationDto gitAuthentication;
}
