package br.com.andersondepaiva.pessoa.dto;

import javax.validation.constraints.NotEmpty;

import br.com.andersondepaiva.core.dto.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class UsuarioPassDto extends BaseDto {

	@NotEmpty(message = "Old Password is required")
	private String oldSenha;

	@NotEmpty(message = "Password is required")
	private String senha;
}
