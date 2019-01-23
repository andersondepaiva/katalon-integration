package br.com.andersondepaiva.katalonintegration.mq.producer.interfaces;

import java.util.UUID;

import org.springframework.amqp.core.Exchange;

import br.com.andersondepaiva.core.mq.IProducer;
import br.com.andersondepaiva.katalonintegration.dto.ProjectDto;

public interface IProjectProducer extends IProducer<ProjectDto, UUID> {

	Exchange projectExchange();

	String getRoutingKeyCmdFillUserChange();

}
