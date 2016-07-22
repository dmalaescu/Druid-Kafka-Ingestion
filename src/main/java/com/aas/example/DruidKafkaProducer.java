package com.aas.example;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Created by mala on 7/22/2016.
 */
public class DruidKafkaProducer {
    private KafkaProducer<String, String> producer;

    public DruidKafkaProducer() throws IOException {
        Properties props = new Properties();
        props.load(ClassLoader.class.getResourceAsStream("/producer.properties"));
        producer = new KafkaProducer<String, String>(props);
    }

    public void sendMessages() {
        for (int i = 0; i < 10; i++) {
            String record = "{\"time\": \"" + new DateTime(DateTimeZone.UTC) + "\", \"url\": \"/foo/bar\", \"user\": \"alice\", \"latencyMs\": 32}";
            producer.send(new ProducerRecord<String, String>("pageviews", record));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }
}
