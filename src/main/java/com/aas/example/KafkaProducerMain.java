package com.aas.example;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by mala on 7/22/2016.
 */
public class KafkaProducerMain {
    public static void main(String[] args) throws IOException {
        System.out.println("Run Kafka Producer");

        DruidKafkaProducer producer = new DruidKafkaProducer();
        producer.sendMessages();
    }
}
