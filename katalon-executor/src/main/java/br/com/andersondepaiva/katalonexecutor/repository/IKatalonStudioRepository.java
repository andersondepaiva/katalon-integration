package br.com.andersondepaiva.katalonexecutor.repository;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.katalonexecutor.model.KatalonStudio;

@Repository
public interface IKatalonStudioRepository extends IRepositoryMongoDb<KatalonStudio, String> {

}
