package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.VaccinationSlotRepo;
import com.example.covidapp.kafka2.VaccineSlotKafka;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class VaccinationSlotService {
    @Autowired
    private VaccinationSlotRepo vaccinationSlotRepo;
    @Autowired
    private VaccinationCentreService vaccinationCentreService;
@Autowired
private VaccineSlotKafka vaccineSlotKafka;

    public String addVaccinationSlot(VaccinationSlots vaccinationSlots) {
        try {
            if( vaccinationSlotRepo
                    .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                            vaccinationSlots.getVaccinecentreid(),
                            vaccinationSlots.getVaccineNo(),
                            vaccinationSlots.getDateNo(),
                            vaccinationSlots.getAgeNo())!=null) {
                log.error("Vaccination slot : {"+vaccinationSlots+" } already exhists");
                return "Already exhists";
            }
            vaccinationSlotRepo.save(vaccinationSlots);
            vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlots);
            log.info("Vaccination Slot Added Successfully: "+vaccinationSlots);
            vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlots);
            return "Vaccination Slot Added Successfully";
        }
        catch (Exception e){
            log.error("Error occured : "+e);
            return e.getMessage();
        }
    }

public String addVaccine(int doseNo,int vaccineCentreId,int vaccineNameNo,int date,int ageNo,int cnt){
    VaccinationSlots vaccinationSlotss=
            vaccinationSlotRepo
                    .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                            vaccineCentreId,vaccineNameNo,date,ageNo);
    if(doseNo==1) {

        vaccinationSlotss.setDoseNo1(vaccinationSlotss.getDoseNo1()+cnt);
        vaccinationSlotRepo.save(vaccinationSlotss);
        vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlotss);

        vaccinationCentreService.addVaccine(vaccineCentreId,cnt,ageNo);
        log.info("Vaccines added Added successfully [ "+
                "vaccineCentreId : "+vaccineCentreId+
                ",vaccineNameNo : "+vaccineNameNo+
                ",date : "+date+
                ",ageNo : "+ageNo+
                ",doseNo : "+doseNo,
                ",cnt : "+cnt+" ]");
        vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlotss);
        return "Vaccines added Added successfully";
    }
    if(doseNo==2) {
        vaccinationSlotss.setDoseNo2(vaccinationSlotss.getDoseNo2()+cnt);
        vaccinationSlotRepo.save(vaccinationSlotss);
        vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlotss);
        vaccinationCentreService.addVaccine(vaccineCentreId,cnt,ageNo);
        log.info("Vaccines added Added successfully [ "+
                        "vaccineCentreId : "+vaccineCentreId+
                        ",vaccineNameNo : "+vaccineNameNo+
                        ",date : "+date+
                        ",ageNo : "+ageNo+
                        ",doseNo : "+doseNo,
                ",cnt : "+cnt+" ]");
        vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlotss);
        return "Vaccines added Added successfully";
    }
    if(doseNo!=1&&doseNo!=2) {
        log.error("only 2 doses are available");
        return "only 2 doses are available";
    }
    log.error("Something went wrong");
    return "Something went wrong";

    }
}
