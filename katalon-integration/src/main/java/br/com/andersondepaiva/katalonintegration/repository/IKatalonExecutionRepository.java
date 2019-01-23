package br.com.andersondepaiva.katalonintegration.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.andersondepaiva.core.repository.IRepositoryMongoDb;
import br.com.andersondepaiva.katalonintegration.model.KatalonExecution;

@Repository
public interface IKatalonExecutionRepository extends IRepositoryMongoDb<KatalonExecution, String> {
	Page<KatalonExecution> findAllByOrderByDataInclusaoDesc(Pageable pageable);

	Page<KatalonExecution> findAllByOrderByDataInclusaoDesc(Example<KatalonExecution> example, Pageable pageable);
}
