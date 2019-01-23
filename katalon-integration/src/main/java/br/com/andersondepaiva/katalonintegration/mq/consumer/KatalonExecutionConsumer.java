package br.com.andersondepaiva.katalonintegration.mq.consumer;

import java.util.UUID;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.mq.Consumer;
import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonExecutionBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonExecutionDto;
import br.com.andersondepaiva.katalonintegration.mq.consumer.interfaces.IKatalonExecutionConsumer;

@Configuration
@Service
public class KatalonExecutionConsumer extends Consumer<KatalonExecutionDto, UUID> implements IKatalonExecutionConsumer {

	@Value("${app.mq.katalon.queue:katalon-cmd-fillUserChange}")
	private String queue;

	@Value("${app.mq.katalon.routing-key-cmd-fillUserChange:katalon.cmd.fill-user-change}")
	private String routingKeyCmdFillUserChange;

	@Autowired
	private IKatalonExecutionBusiness katalonBusiness;

	@Bean
	public Queue katalonCmdExecuteQueue() {
		return buildQueue(queue);
	}

	@Bean
	public Binding bindingKatalonExecutionConsumer(Queue katalonCmdExecuteQueue, Exchange katalonExchange) {
		return BindingBuilder.bind(katalonCmdExecuteQueue).to(katalonExchange).with(routingKeyCmdFillUserChange)
				.noargs();
	}

	@Override
	@RabbitListener(queues = "#{katalonCmdExecuteQueue.getName()}")
	public void receive(String json) throws Exception {
		KatalonExecutionDto dto = jsonService.getObjectMapper().readValue(json, KatalonExecutionDto.class);
		katalonBusiness.fillUserChange(dto.getId());
	}
}
