package com.example.good;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
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

import com.example.demo.TopicApplication;

@SpringBootApplication
@Configuration
@EnableRabbit
public class DirectApplication {
	
	public static final String QUEUE_NAME = "queue";

	@Autowired
	private Environment env;
    
	@Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();

        factory.setPort(Integer.parseInt(env.getProperty("rabbitmq.port")));
        factory.setUsername(env.getProperty("rabbitmq.user"));
        factory.setPassword(env.getProperty("rabbitmq.password"));
        factory.setHost(env.getProperty("rabbitmq.ip"));

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        
        template.setExchange(env.getProperty("rabbitmq.exchange"));
        template.setRoutingKey(env.getProperty("rabbitmq.routingKey"));
        template.setConnectionFactory(connectionFactory());

        return template;
    }
    
    @Bean
    MessageListenerAdapter listenerAdapter(DirectReceiver receiver) {
    	return new MessageListenerAdapter(receiver, "onMessage");
    }
    
    @Bean
    public SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setQueueNames(env.getProperty("rabbitmq.queueName"));
        container.setMessageListener(listenerAdapter(new DirectReceiver()));
        container.setReceiveTimeout(3000L);			// Receive Timeout default 1 sec
        container.setRecoveryInterval(5000L);		// Recovery Interval default 5 sec
                
        return container;
    }
    
    @Bean
    public Queue queue() {
        return new Queue(env.getProperty("rabbitmq.queueName"), false);		// true : disk, false : ram
    }
    
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(env.getProperty("rabbitmq.exchange"));
    }
    
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(env.getProperty("rabbitmq.exchange"));
    }
    
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(env.getProperty("rabbitmq.queueName"));
    }
    
	public static void main(String[] args) {
		SpringApplication.run(TopicApplication.class, args);
	}

}
