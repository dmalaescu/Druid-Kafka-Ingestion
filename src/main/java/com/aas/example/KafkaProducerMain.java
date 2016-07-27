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
        String record = "{\"time\": \"" + new DateTime(DateTimeZone.UTC) + "\", \"url\": \"/foo/bar\", \"user\": \"user1\", \"latencyMs\": 32}";
        System.out.println("This is a sample of record\n" + record);
        DruidKafkaProducer producer = new DruidKafkaProducer();
        producer.sendMessages();
    }
}
