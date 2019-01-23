package br.com.andersondepaiva.katalonintegration.mq.producer;

import java.util.UUID;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.mq.Producer;
import br.com.andersondepaiva.katalonintegration.dto.ProjectDto;
import br.com.andersondepaiva.katalonintegration.mq.producer.interfaces.IProjectProducer;

@Configuration
@Service
public class ProjectProducer extends Producer<ProjectDto, UUID> implements IProjectProducer {
	@Value("${app.mq.project.exchange:project}")
	private String exchange;

	@Value("${app.mq.project.routing-key-cmd-fillUserChange:project.cmd.fill-user-change}")
	private String routingKeyCmdFillUserChange;

	public String getRoutingKeyCmdFillUserChange() {
		return routingKeyCmdFillUserChange;
	}

	@Bean
	public Exchange projectExchange() {
		// TODO Auto-generated method stub
		return new TopicExchange(exchange);
	}
}
