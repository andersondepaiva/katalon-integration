package br.com.andersondepaiva.katalonexecutor.repository;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.katalonexecutor.model.Project;

@Repository
public interface IProjectRepository extends IRepositoryMongoDb<Project, String> {
	Long countByNameIgnoreCaseAndExcluidoFalse(String name);
}
