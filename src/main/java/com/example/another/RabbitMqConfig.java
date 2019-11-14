package com.example.another;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
@PropertySource("classpath:/application.properties")
public class RabbitMqConfig {
    @Autowired private Environment env;
 
    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setPort(Integer.parseInt(env.getProperty("rabbitmq.port")));
        connectionFactory.setUsername(env.getProperty("rabbitmq.user"));
        connectionFactory.setPassword(env.getProperty("rabbitmq.password"));
        connectionFactory.setHost(env.getProperty("rabbitmq.ip"));

        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
         RabbitTemplate rabbitTemplate = new RabbitTemplate();
         rabbitTemplate.setExchange(env.getProperty("rabbitmq.exchange"));
         rabbitTemplate.setRoutingKey(env.getProperty("rabbitmq.routingKey"));
         //rabbitTemplate.setQueue(env.getProperty("rabbitmq.queueName"));
         rabbitTemplate.setConnectionFactory(new CachingConnectionFactory(connectionFactory()));
         return rabbitTemplate;
    }
 
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(new CachingConnectionFactory(connectionFactory()));
        container.setQueueNames(env.getProperty("rabbitmq.queueName"));
        container.setMessageListener(exampleListener());
        return container;
    }


    @Bean
    public MessageListener exampleListener() {
        return new MessageListener() {
            public void onMessage(Message message) {
                System.out.println("received: " + new String(message.getBody()));
            }
        };
    }
 
}