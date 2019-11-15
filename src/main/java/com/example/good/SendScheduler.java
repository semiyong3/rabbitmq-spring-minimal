package com.example.good;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.example.demo.TopicApplication;

@Component
public class SendScheduler {
	
	@Autowired
	private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(SendScheduler.class);
    
    @Scheduled(cron = "0/3 * * * * *")
    public void onSend() {
        logger.info("Sending message... Start");
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        IntStream.range(1, 15000)
                    .parallel()
                    .forEach(val -> {
                        rabbitTemplate.convertAndSend(env.getProperty("rabbitmq.routingKey"), "Hello~~~~~~~~, RabbitMQ! 1");
                    });
        stopWatch.stop();
        logger.info(stopWatch.toString());
        logger.info("Sending message... End");
    }
}