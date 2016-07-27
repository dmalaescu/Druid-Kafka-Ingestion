package com.aas.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * Created by mala on 7/22/2016.
 */
public class DruidKafkaProducer {
    private KafkaProducer<String, String> producer;
    static List<String> users = new ArrayList<>();
    static {
        users.add("alice");
        users.add("bob");
        users.add("mike");
        users.add("jhon");
    }

    private static List<String> urls = new ArrayList<>();
    static {
        urls.add("/foo/bar");
        urls.add("/wikipedia");
        urls.add("/google");
        urls.add("/yahoo");
    }

    private int nrMessages;

    public DruidKafkaProducer(int nrMessages) throws IOException {
        Properties props = new Properties();
        props.load(ClassLoader.class.getResourceAsStream("/producer.properties"));
        producer = new KafkaProducer<>(props);
        this.nrMessages = nrMessages;
    }

    public void sendMessages() throws JsonProcessingException {
        Random random = new Random();
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < nrMessages; i++) {
            Record record = new Record();
            record.setTime(new DateTime(DateTimeZone.UTC).toString());
            record.setUrl(urls.get(random.nextInt(urls.size())));
            record.setUser(users.get(random.nextInt(users.size())));
            record.setLatencyMs(random.nextInt(100));
            System.out.println(objectMapper.writeValueAsString(record));
            // send to Kafka Produces
            producer.send(new ProducerRecord("pageviews", objectMapper.writeValueAsString(record)));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }
}
