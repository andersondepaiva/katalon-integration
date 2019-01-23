package br.com.andersondepaiva.katalonintegration.repository;

import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.katalonintegration.model.KatalonStudio;

@Repository
public interface IKatalonStudioRepository extends IRepositoryMongoDb<KatalonStudio, String> {

}
