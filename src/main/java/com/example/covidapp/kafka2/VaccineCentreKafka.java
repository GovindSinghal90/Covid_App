package com.example.covidapp.kafka2;

import com.example.covidapp.models.done.VaccinationCentre;
import com.example.covidapp.models.done.VaccineName;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@Log4j2
public class VaccineCentreKafka {

    //.................
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void saveCreateVaccineCentreLog(VaccinationCentre vaccinationCentre)
    {
        log.info(String.format("Vaccine centre created -> %s", vaccinationCentre));
        //this.kafkaTemplate.send("Vaccination_centre", vaccinationCentre);
        this.kafkaTemplate.send("t1", vaccinationCentre);
    }
//......................



//    @KafkaListener(topics = "Covid_topic",
//            groupId = "Covid_1991")
//    public void consume(User user)
//    {
//        log.info(String.format("User created -> %s", user));
//    }
}
