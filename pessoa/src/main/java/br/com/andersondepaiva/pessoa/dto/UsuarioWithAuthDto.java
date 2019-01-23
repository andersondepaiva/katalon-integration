package br.com.andersondepaiva.pessoa.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class UsuarioWithAuthDto extends UsuarioDto {

	@NotEmpty(message = "Password is required")
	private String senha;
}
