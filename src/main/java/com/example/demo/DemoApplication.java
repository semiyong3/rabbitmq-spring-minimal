package com.example.demo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DemoApplication {

	static final String topicExchangeName = "spring-boot-exchange";

	static final String queueName = "spring-boot";

	@Autowired
	private Environment env;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		//CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		/*
		 * connectionFactory.setPort(Integer.parseInt(env.getProperty("rabbitmq.port")))
		 * ; connectionFactory.setUsername(env.getProperty("rabbitmq.user"));
		 * connectionFactory.setPassword(env.getProperty("rabbitmq.password"));
		 * connectionFactory.setHost(env.getProperty("rabbitmq.ip"));
		 */	    connectionFactory.setPort(5672);
		connectionFactory.setUsername("rabbitmq");
		connectionFactory.setPassword("alsgml");
		connectionFactory.setHost("127.0.0.1");

		return connectionFactory;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
