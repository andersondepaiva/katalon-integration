package br.com.andersondepaiva.pessoa.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.andersondepaiva.core.dto.BaseDto;
import br.com.andersondepaiva.pessoa.model.Perfil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class UsuarioDto extends BaseDto {

	@NotEmpty(message = "Login is required")
	@Email(message = "Invalid Email")
	private String login;

	@NotNull(message = "Profile User is required")
	private Perfil tipoUsuario;

	@Valid
	private PessoaDto pessoa;
}
