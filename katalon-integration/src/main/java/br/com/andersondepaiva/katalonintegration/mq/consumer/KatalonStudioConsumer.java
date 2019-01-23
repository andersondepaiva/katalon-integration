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
import br.com.andersondepaiva.katalonintegration.business.interfaces.IKatalonStudioBusiness;
import br.com.andersondepaiva.katalonintegration.dto.KatalonStudioDto;
import br.com.andersondepaiva.katalonintegration.mq.consumer.interfaces.IKatalonStudioConsumer;

@Configuration
@Service
public class KatalonStudioConsumer extends Consumer<KatalonStudioDto, UUID> implements IKatalonStudioConsumer {

	@Value("${app.mq.katalon-studio.queue:katalonStudio-cmd-fillUserChange}")
	private String queue;

	@Value("${app.mq.katalon-studio.routing-key-cmd-fillUserChange:katalon-studio.cmd.fill-user-change}")
	private String routingKeyCmdFillUserChange;

	@Autowired
	private IKatalonStudioBusiness katalonStudioBusiness;

	@Bean
	public Queue katalonStudioCmdExecuteQueue() {
		return buildQueue(queue);
	}

	@Bean
	public Binding bindingKatalonStudioConsumer(Queue katalonStudioCmdExecuteQueue, Exchange katalonStudioExchange) {
		return BindingBuilder.bind(katalonStudioCmdExecuteQueue).to(katalonStudioExchange)
				.with(routingKeyCmdFillUserChange).noargs();
	}

	@Override
	@RabbitListener(queues = "#{katalonStudioCmdExecuteQueue.getName()}")
	public void receive(String json) throws Exception {
		KatalonStudioDto dto = jsonService.getObjectMapper().readValue(json, KatalonStudioDto.class);
		katalonStudioBusiness.fillUserChange(dto.getId());
	}
}
