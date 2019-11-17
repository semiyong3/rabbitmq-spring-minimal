package com.example.good;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

//@Component
public class DirectRunner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
    private final DirectReceiver receiver;

    @Autowired
	private Environment env;

    public DirectRunner(DirectReceiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
/*
    	System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(env.getProperty("rabbitmq.routingKey"), "Hello~~~~~~~~, This is Direct Binding!");
*/        
    }

}