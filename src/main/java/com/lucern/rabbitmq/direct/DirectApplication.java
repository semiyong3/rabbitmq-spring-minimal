package com.lucern.rabbitmq.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Configuration
public class DirectApplication {

	static final String directExchangeName = "spring-boot-direct-exchange";
	static final String queueName = "spring-boot-direct-queue";
	static final String routingKey = "direct.routing.key";
	static final int sendCount = 100000;

	@Autowired
	private Environment env;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(directExchangeName);
	}

	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(routingKey);
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		
		connectionFactory.setHost(env.getProperty("rabbitmq.ip"));
		connectionFactory.setPort(Integer.parseInt(env.getProperty("rabbitmq.port")));
		connectionFactory.setUsername(env.getProperty("rabbitmq.user"));
		connectionFactory.setPassword(env.getProperty("rabbitmq.password"));
		
		return connectionFactory;
	}

	@Bean
	SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(queueName);
		container.setMessageListener(directListenerAdapter());
		
		return container;
	}
	
	@Bean 
	public RabbitTemplate rabbitTemplate() { 
		RabbitTemplate template = new RabbitTemplate();
		  
		template.setExchange(directExchangeName);
		template.setRoutingKey(routingKey);
		template.setConnectionFactory(connectionFactory());
		  
	    return template; 
	}
	 
    @Bean
	MessageListenerAdapter directListenerAdapter() {
		return new MessageListenerAdapter(new Receiver(), "receiveMessage");
	}

	public static void main(String[] args) {
		 SpringApplication.run(DirectApplication.class, args); 
	}
	  
}
