package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.config.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.model.CreateProductRestModel;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService{

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER =  LoggerFactory.getLogger(this.getClass());

    public  ProductServiceImpl(KafkaTemplate<String , ProductCreatedEvent> kafkaTemplate){

        this.kafkaTemplate=kafkaTemplate;
    }


    // SYNCHRONOUS PUBLISHING OF MESSAGE TO KAFKA BROKER

    @Override
    public String createProductBySync(CreateProductRestModel productRestModel) throws  Exception{


        String productId = UUID.randomUUID().toString();

        // TODO : Persist Product Details into database table before publishing an event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity()
        );


        LOGGER.info("Before publishing a ProductCreateEvent");


       /* SendResult<String, ProductCreatedEvent> result
                = kafkaTemplate.send("product-created-events-topic",
                productId, productCreatedEvent).get();*/


        // it will give the access to work with message headers
        ProducerRecord<String, ProductCreatedEvent>  record = new ProducerRecord<>(
                "product-created-events-topic",
                productId,
                productCreatedEvent
        );

        record.headers().add("messageID ", UUID.randomUUID().toString().getBytes());

        SendResult<String, ProductCreatedEvent> result
                = kafkaTemplate.send(record).get();   // to work with min.insync.replicas


        // If we add the below join method it will become synchronous communication
        //future.join();


        LOGGER.info("Topic: {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset: {}", result.getRecordMetadata().offset());

        LOGGER.info("******* Returning product id ****************");

        return productId;
    }



       //  ASYNCHRONOUS PUBLISHING OF MESSAGE TO KAFKA BROKER


    @Override
    public String createProductByAsync(CreateProductRestModel productRestModel) {

        String productId = UUID.randomUUID().toString();

        // TODO : Persist Product Details into database table before publishing an event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity()
        );

        // due to completabelfuture the message is Asynchronous

         CompletableFuture<SendResult<String, ProductCreatedEvent>> future
                 = kafkaTemplate.send("product-created-events-topic",
                                                    productId, productCreatedEvent);


         future.whenComplete((result, exception)->{

             if(exception != null){

                 LOGGER.error("Failed to send message : {}", exception.getMessage());
             }else{

                 LOGGER.info("Message was sent successfully : {}", result.getRecordMetadata());
             }

         });



         LOGGER.info("******* Returning product id ****************");

        return productId;
    }




}
