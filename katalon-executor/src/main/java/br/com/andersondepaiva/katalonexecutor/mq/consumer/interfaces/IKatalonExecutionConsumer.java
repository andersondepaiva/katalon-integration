package br.com.andersondepaiva.katalonexecutor.mq.consumer.interfaces;

import java.util.UUID;

import br.com.andersondepaiva.core.mq.IConsumer;
import br.com.andersondepaiva.katalonexecutor.dto.KatalonExecutionDto;

public interface IKatalonExecutionConsumer extends IConsumer<KatalonExecutionDto, UUID> {

}
