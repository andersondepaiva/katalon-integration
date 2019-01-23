package br.com.andersondepaiva.katalonintegration.repository;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.katalonintegration.model.Project;

@Repository
public interface IProjectRepository extends IRepositoryMongoDb<Project, String> {
	Long countByNameIgnoreCaseAndExcluidoFalse(String name);
}
