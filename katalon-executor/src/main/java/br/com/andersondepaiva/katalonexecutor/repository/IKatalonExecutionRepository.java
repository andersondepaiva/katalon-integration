package br.com.andersondepaiva.katalonexecutor.repository;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.katalonexecutor.model.KatalonExecution;

@Repository
public interface IKatalonExecutionRepository extends IRepositoryMongoDb<KatalonExecution, String> {

}
