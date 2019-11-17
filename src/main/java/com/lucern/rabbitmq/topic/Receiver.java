package com.lucern.rabbitmq.topic;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
	private CountDownLatch latch = new CountDownLatch(TopicApplication.sendCount);
	
	public void receiveMessage(String msg) {
		System.out.println("Received message <" + msg + ">");
		latch.countDown();
	}
	
	public CountDownLatch getLatch() {
		return latch;
	}
}
