package com.example.good;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectReceiver {
 
    private static final Logger logger = LoggerFactory.getLogger(DirectReceiver.class);
    
    @RabbitListener(queues = DirectApplication.QUEUE_NAME)
    public void onMessage(String msg) {
        logger.info("Received < " + msg.toString() + " >");
    }
}

