package br.com.andersondepaiva.katalonintegration.mq.producer;

import java.util.UUID;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.mq.Producer;
import br.com.andersondepaiva.katalonintegration.dto.KatalonStudioDto;
import br.com.andersondepaiva.katalonintegration.mq.producer.interfaces.IKatalonStudioProducer;

@Configuration
@Service
public class KatalonStudioProducer extends Producer<KatalonStudioDto, UUID> implements IKatalonStudioProducer {
	@Value("${app.mq.katalon-studio.exchange:katalon-studio}")
	private String exchange;

	@Value("${app.mq.katalon-studio.routing-key-cmd-fillUserChange:katalon-studio.cmd.fill-user-change}")
	private String routingKeyCmdFillUserChange;

	public String getRoutingKeyCmdFillUserChange() {
		return routingKeyCmdFillUserChange;
	}

	@Bean
	public Exchange katalonStudioExchange() {
		// TODO Auto-generated method stub
		return new TopicExchange(exchange);
	}
}
