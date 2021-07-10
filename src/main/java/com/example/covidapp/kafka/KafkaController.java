package com.example.covidapp.kafka;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class KafkaController {
    @Autowired
    private KafkaProducerService kafkaProducerService;


//    public class A{
//        private int id=1;
//        private String name="Govind";
//    }
    @GetMapping("/test")
    public String fun(){
       // A a=new A();
        kafkaProducerService.produceKafkaEvent("hi","Covid_topic");
        return "published";
    }
//    @KafkaListener(topics = "Covid_topic")
//    public void receive(final A a) {
//
//        try {
//            log.info("recieved ", a.id, a.name);
//        } catch (Exception ex) {
//            log.error("The data could not be processed. Exception: {}", ex);
//        }
//
//    }

}
