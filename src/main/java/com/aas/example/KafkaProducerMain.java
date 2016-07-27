package com.aas.example;

import java.io.IOException;

/**
 * Created by mala on 7/22/2016.
 */
public class KafkaProducerMain {
    public static void main(String[] args) throws IOException {
        System.out.println("Run Kafka Producer");
        DruidKafkaProducer producer = new DruidKafkaProducer(Integer.parseInt(args[0]));
        producer.sendMessages();

    }
}
