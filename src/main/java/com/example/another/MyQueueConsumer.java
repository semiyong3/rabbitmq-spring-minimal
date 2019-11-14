package com.example.another;

import java.io.IOException;
import java.util.UUID;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



public class MyQueueConsumer extends DefaultConsumer {
    
    Channel channel;
    public MyQueueConsumer(Channel channel) {
           super(channel);
           // TODO Auto-generated constructor stub
           this.channel = channel;
    }
    @Override
    public void handleDelivery(String consumeTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                                                    byte[] body)
           throws IOException
    {
           String routingKey = envelope.getRoutingKey();
           String contentType = properties.getContentType();
           long deliveryTag = envelope.getDeliveryTag();
          
           // message handling logic here
           String msg = new String(body);
           UUID uuid = UUID.randomUUID();
           System.out.println(uuid+" S Channel :"+channel+" Thread:"+Thread.currentThread()+" msg:"+msg);
          
           // multiple - false if we are acknowledging multiple messages with the same delivery tag
           this.channel.basicAck(deliveryTag, false);
    }
}