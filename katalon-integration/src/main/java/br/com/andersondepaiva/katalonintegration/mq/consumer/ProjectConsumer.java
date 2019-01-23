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
import br.com.andersondepaiva.katalonintegration.business.interfaces.IProjectBusiness;
import br.com.andersondepaiva.katalonintegration.dto.ProjectDto;
import br.com.andersondepaiva.katalonintegration.mq.consumer.interfaces.IProjectConsumer;

@Configuration
@Service
public class ProjectConsumer extends Consumer<ProjectDto, UUID> implements IProjectConsumer {

	@Value("${app.mq.project.queue:project-cmd-fillUserChange}")
	private String queue;

	@Value("${app.mq.project.routing-key-cmd-fillUserChange:project.cmd.fill-user-change}")
	private String routingKeyCmdFillUserChange;

	@Autowired
	private IProjectBusiness projectBusiness;

	@Bean
	public Queue projectCmdExecuteQueue() {
		return buildQueue(queue);
	}

	@Bean
	public Binding bindingProjectConsumer(Queue projectCmdExecuteQueue, Exchange projectExchange) {
		return BindingBuilder.bind(projectCmdExecuteQueue).to(projectExchange).with(routingKeyCmdFillUserChange)
				.noargs();
	}

	@Override
	@RabbitListener(queues = "#{projectCmdExecuteQueue.getName()}")
	public void receive(String json) throws Exception {
		ProjectDto dto = jsonService.getObjectMapper().readValue(json, ProjectDto.class);
		projectBusiness.fillUserChange(dto.getId());
	}
}
