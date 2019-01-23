package br.com.andersondepaiva.pessoa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class UsuarioFiltroDto {
	private String nome;
}
