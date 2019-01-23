package br.com.andersondepaiva.pessoa.model;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.andersondepaiva.core.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuppressWarnings("serial")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Document(collection = "usuarios")
public class Usuario extends BaseModel {

	private String senha;
	private Perfil tipoUsuario;
	private String login;
	private Pessoa pessoa;
}
