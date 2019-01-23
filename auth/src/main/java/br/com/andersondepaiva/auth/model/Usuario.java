package br.com.andersondepaiva.auth.model;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.andersondepaiva.auth.security.Perfil;
import br.com.andersondepaiva.core.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Document(collection = "usuarios")
@Getter
@Setter
public class Usuario extends BaseModel {

	private String senha;
	private Perfil tipoUsuario;
	private String login;
	private String foto;
}
