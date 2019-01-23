package br.com.andersondepaiva.katalonintegration.mq.producer.interfaces;

import java.util.UUID;

import org.springframework.amqp.core.Exchange;

import br.com.andersondepaiva.core.mq.IProducer;
import br.com.andersondepaiva.katalonintegration.dto.KatalonStudioDto;

public interface IKatalonStudioProducer extends IProducer<KatalonStudioDto, UUID> {

	Exchange katalonStudioExchange();

	String getRoutingKeyCmdFillUserChange();

}
