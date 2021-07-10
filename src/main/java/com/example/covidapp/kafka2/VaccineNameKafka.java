package com.example.covidapp.kafka2;

import com.example.covidapp.models.done.VaccineName;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@Log4j2
public class VaccineNameKafka {

    //.................
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void saveCreateVaccineNameLog(VaccineName vaccineName)
    {
        log.info(String.format("Vaccine name created -> %s", vaccineName));
        this.kafkaTemplate.send("Vaccine_name", vaccineName);
    }
//......................



//    @KafkaListener(topics = "Covid_topic",
//            groupId = "Covid_1991")
//    public void consume(User user)
//    {
//        log.info(String.format("User created -> %s", user));
//    }
}
