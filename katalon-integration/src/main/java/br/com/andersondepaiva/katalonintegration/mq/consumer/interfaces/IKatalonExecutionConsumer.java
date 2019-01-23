package br.com.andersondepaiva.katalonintegration.mq.consumer.interfaces;

import java.util.UUID;

import br.com.andersondepaiva.core.mq.IConsumer;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;

public interface IKatalonExecutionConsumer extends IConsumer<KatalonExecutionDto, UUID> {
}
