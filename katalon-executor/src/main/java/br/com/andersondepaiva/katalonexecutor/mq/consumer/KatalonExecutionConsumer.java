package br.com.andersondepaiva.katalonexecutor.mq.consumer;

import java.util.UUID;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.mq.Consumer;
import br.com.andersondepaiva.katalonexecutor.business.interfaces.IKatalonExecutionBusiness;
import br.com.andersondepaiva.katalonexecutor.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonexecutor.mq.consumer.interfaces.IKatalonExecutionConsumer;

@Configuration
@Service
public class KatalonExecutionConsumer extends Consumer<KatalonExecutionDto, UUID> implements IKatalonExecutionConsumer {

	@Value("${app.mq.katalon.exchange:katalon}")
	private String exchange;

	@Value("${app.mq.katalon.queue:katalon-cmd-queue}")
	private String queue;

	@Value("${app.mq.katalon.routing-key-cmd-execute:katalon.cmd.execute}")
	private String routingKey;

	@Autowired
	private IKatalonExecutionBusiness katalonBusiness;

	@Bean
	public Queue katalonCmdExecuteQueue() {
		return buildQueue(queue);
	}

	@Bean
	public Exchange katalonExchange() {
		// TODO Auto-generated method stub
		return new TopicExchange(exchange);
	}

	@Bean
	public Binding bindingKatalonExecutionConsumer(Queue katalonCmdExecuteQueue, Exchange katalonExchange) {
		return BindingBuilder.bind(katalonCmdExecuteQueue).to(katalonExchange).with(routingKey).noargs();
	}

	@Override
	@RabbitListener(queues = "#{katalonCmdExecuteQueue.getName()}")
	public void receive(String json) throws Exception {
		KatalonExecutionDto dto = jsonService.getObjectMapper().readValue(json, KatalonExecutionDto.class);
		katalonBusiness.executeTests(dto);
	}
}
