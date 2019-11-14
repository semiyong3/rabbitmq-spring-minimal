package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.another.RabbitMqConfig;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {RabbitMqConfig.class})
public class ProducerTest {
    @Autowired
    RabbitTemplate rabbitTemplate;
 
    @Test
    public void productTest(){
        rabbitTemplate.convertAndSend("Hello World");
    }
}
