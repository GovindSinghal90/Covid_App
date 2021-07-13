package com.example.covidapp.kafka2;

import com.example.covidapp.models.done.VaccinationCentre;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@Log4j2
public class VaccineSlotKafka {

    //.................
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void saveCreateVaccineSlotLog(VaccinationSlots vaccinationSlot)
    {
        log.info(String.format("Vaccine centre created -> %s", vaccinationSlot));
        //this.kafkaTemplate.send("Vaccination_slot", vaccinationSlot);
        this.kafkaTemplate.send("vaccineSlotKafka", vaccinationSlot);
    }
//......................



//    @KafkaListener(topics = "Covid_topic",
//            groupId = "Covid_1991")
//    public void consume(User user)
//    {
//        log.info(String.format("User created -> %s", user));
//    }
}
