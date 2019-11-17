package com.lucern.rabbitmq.direct;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending start message...");

        for (int i=0; i<DirectApplication.sendCount; i++) {
        	rabbitTemplate.convertAndSend(DirectApplication.directExchangeName, DirectApplication.routingKey, " count = " + i + ", exchangeName = "
        			+ DirectApplication.directExchangeName + ", routingKey = " + DirectApplication.routingKey +  ", message = hello lucern!!");
        }
        
        System.out.println("Sending end message...");
       
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);

        System.out.println("Receiving end message...");

    }
}