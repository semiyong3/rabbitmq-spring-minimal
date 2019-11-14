package com.example.demo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.another.MyQueueConsumer;
import com.example.another.RabbitMqConfig;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = {RabbitMqConfig.class})
public class ConsumerTest{
    
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired 
    RabbitTemplate rabbitTemplate;
    @Autowired
    ConnectionFactory connectionFactory;
    
    @Test
    public void consumTest(){
        int maxthread =20;
        ExecutorService es = Executors.newFixedThreadPool(maxthread);
        try {
            Connection conn = connectionFactory.newConnection(es);

            // Thread 당 다른 Channel 을 사용하기 위해서 Thread수 만큼 별도의 채널을 생성하낟.
           for(int i=0;i<maxthread;i++){
                   Channel channel = conn.createChannel();     
                   channel.basicQos(1);
                   channel.basicConsume("test",false,new MyQueueConsumer(channel));
           }
           System.out.println("Invoke "+maxthread+" thread and wait for listening");   
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(rabbitTemplate.receiveAndConvert());
        //System.exit(1);
    }
}