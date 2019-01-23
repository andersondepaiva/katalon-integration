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
public class PessoaDto extends BaseDto {

	@NotEmpty(message = "Name is required")
	private String nome;
}
