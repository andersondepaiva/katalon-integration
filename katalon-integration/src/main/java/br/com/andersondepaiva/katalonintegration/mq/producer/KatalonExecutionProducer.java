package br.com.andersondepaiva.katalonintegration.mq.producer;

import java.util.UUID;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.mq.Producer;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonintegration.mq.producer.interfaces.IKatalonExecutionProducer;

@Configuration
@Service
public class KatalonExecutionProducer extends Producer<KatalonExecutionDto, UUID> implements IKatalonExecutionProducer {
	@Value("${app.mq.katalon.exchange:katalon}")
	private String exchange;

	@Value("${app.mq.katalon.routing-key-cmd-execute:katalon.cmd.execute}")
	private String routingKeyCmdExecute;

	@Value("${app.mq.katalon.routing-key-cmd-fillUserChange:katalon.cmd.fill-user-change}")
	private String routingKeyCmdFillUserChange;

	public String getRoutingKeyCmdExecute() {
		return routingKeyCmdExecute;
	}

	public String getRoutingKeyCmdFillUserChange() {
		return routingKeyCmdFillUserChange;
	}

	@Bean
	public Exchange katalonExchange() {
		// TODO Auto-generated method stub
		return new TopicExchange(exchange);
	}
}
