package br.com.andersondepaiva.katalonintegration.mq.producer.interfaces;

import java.util.UUID;

import org.springframework.amqp.core.Exchange;

import br.com.andersondepaiva.core.mq.IProducer;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;

public interface IKatalonExecutionProducer extends IProducer<KatalonExecutionDto, UUID> {

	Exchange katalonExchange();

	String getRoutingKeyCmdExecute();
	
	String getRoutingKeyCmdFillUserChange();

}
