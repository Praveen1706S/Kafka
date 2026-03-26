package com.appsdeveloperblog.ws.emailnotifications.handler;

import com.appsdeveloperblog.ws.emailnotifications.config.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductCreatedHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //@KafkaHandler
    @KafkaListener(topics = "product-created-events-topic", groupId = "product-created-events")
    public void handler(ProductCreatedEvent productCreatedEvent){

        System.out.println("Received a new event : " + productCreatedEvent.getTitle());

        LOGGER.info("Received a new event : {}", productCreatedEvent.getTitle());

    }
}
