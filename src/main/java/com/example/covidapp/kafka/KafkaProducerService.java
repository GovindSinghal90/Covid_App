package com.example.covidapp.kafka;


import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Future;

@Log4j2
@Service
public class KafkaProducerService {
    @Autowired
    private KafkaProducerConfig kafkaProducer;

//    @Autowired
//    public KafkaProducerService(final KafkaProducer<String, String> kafkaProducer) {
//        this.kafkaProducer = kafkaProducer;
//    }
    public void produceKafkaEvent(String message, final String topic) {
        log.info("Kafka send : Publishing to topic : '" + topic + "' with message : '" + message);
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, message);
        final Future<RecordMetadata> future = kafkaProducer.kafkaProducer().send(producerRecord, new Callback() {
            @Override
            public void onCompletion(final RecordMetadata recordMetadata, final Exception e) {
                if (Objects.isNull(e))
                    log.info("Sent with message ='{}' and offset={}", message,
                            recordMetadata.offset());
                else
                    log.error("unable to send message ='{}'", message, e);
            }
        });
//        log.info("Kafka send : Publishing to topic : '" + topic + "' with message : '" + message);
//        try {
//            final ProducerRecord<String, KafkaController.A> producerRecord = new ProducerRecord<>(topic, message);
//            kafkaProducer.kafkaProducer().send(producerRecord);
//            log.info("Sent with message ='{}' and offset={}", message, topic);
//        }
//        catch (Exception e){
//            log.error("unable to send message ='{}'", message, e);
//        }
//        finally {
//            kafkaProducer.kafkaProducer().flush();
//        }

    }
}
