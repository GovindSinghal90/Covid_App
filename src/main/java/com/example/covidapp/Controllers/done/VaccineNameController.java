package com.example.covidapp.Controllers.done;

import com.example.covidapp.Repo.done.VaccineNameRepo;
import com.example.covidapp.Services.done.AdminService;
import com.example.covidapp.kafka2.VaccineNameKafka;
import com.example.covidapp.models.done.VaccineName;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class VaccineNameController {
    @Autowired
    private VaccineNameRepo vaccineNameRepo;
    @Autowired
    private AdminService adminService;
@Autowired
private VaccineNameKafka vaccineNameKafka;

    @PostMapping("/vaccine/add")
    public String addVaccine(@RequestBody(required = false) VaccineName vaccineName,
                             @RequestHeader(required = false)String token){
        log.info("Vaccine Name Controller called:: /vaccine/add with vaccine name :"+vaccineName+" called");
        if (token == null) {
            log.error("Token Not Present in headers");
            return "Token Not Present in headers";
        }
        if (adminService.isAdminAuthenticated(token) == -1) {
            log.error("Not Authenticated");
            return "Not Authenticated";
        }
        if(vaccineName==null){
            log.error("vaccine name is null passed");
            return "Please add vaccine name required fields";
        }
        if(vaccineName.getVaccineName()==null||vaccineName.getVaccineName().length()==0) {
            log.error("empty vaccine name");
            return "empty vaccine name";
        }
        try {
            if(vaccineNameRepo.findByVaccineName(vaccineName.getVaccineName())!=null) {
                log.error("Vaccine already Exhists with name :"+vaccineName.getVaccineName() );
                return "Vaccine already Exhists";
            }

            vaccineNameRepo.save(vaccineName);
            //kafkaProducerService.produceKafkaEvent(,"myEvent");
            vaccineNameKafka.saveCreateVaccineNameLog(vaccineName);
            log.info("Vaccine Added Successfully with details { "+vaccineName+" }");

            return "Vaccine Added Successfully";
        }
        catch (Exception e){
            log.error("Error occured"+e);
            return e.getMessage();
        }
    }
}
