package br.com.andersondepaiva.auth.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.auth.model.Usuario;
import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;

@Repository
public interface IUsuarioRepository extends IRepositoryMongoDb<Usuario, String> {

	List<Usuario> findByLoginAndExcluidoFalse(String login);

}
