package com.lucern.rabbitmq.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
public class TopicApplication {

	static final String topicExchangeName = "spring-boot-topic-exchange";
	static final String queueName = "spring-boot-topic-queue";
	static final String topic = "foo.bar.#";
	static final String routingKey = "foo.bar.bus";
	static final int sendCount = 100000;

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
		return BindingBuilder.bind(queue).to(exchange).with(topic);
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
		container.setMessageListener(topicListenerAdapter());
		
		return container;
	}
	
	@Bean 
	public RabbitTemplate rabbitTemplate() { 
		RabbitTemplate template = new RabbitTemplate();
		  
		template.setExchange(topicExchangeName);
		template.setRoutingKey("foo.bar.bus");
		template.setConnectionFactory(connectionFactory());
		  
	    return template; 
	}
	 
    @Bean
	MessageListenerAdapter topicListenerAdapter() {
		return new MessageListenerAdapter(new Receiver(), "receiveMessage");
	}

	public static void main(String[] args) {
		 SpringApplication.run(TopicApplication.class, args); 
	}
	  
}
