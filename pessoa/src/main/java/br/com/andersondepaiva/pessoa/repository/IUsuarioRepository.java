package br.com.andersondepaiva.pessoa.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.pessoa.model.Usuario;

@Repository
public interface IUsuarioRepository extends IRepositoryMongoDb<Usuario, String> {

	List<Usuario> findByLoginAndExcluidoFalse(String login);

}
