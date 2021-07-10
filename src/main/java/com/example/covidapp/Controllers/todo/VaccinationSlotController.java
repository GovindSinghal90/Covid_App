package com.example.covidapp.Controllers.todo;

import com.example.covidapp.Repo.done.*;
import com.example.covidapp.Services.done.AdminService;
import com.example.covidapp.Services.done.VaccinationSlotService;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class VaccinationSlotController {
    @Autowired
    VaccinationSlotService vaccinationSlotService;
    @Autowired
    VaccinationSlotRepo vaccinationSlotRepo;
    @Autowired
    VaccinationCentreRepo vaccinationCentreRepo;
    @Autowired
    VaccineNameRepo vaccineNameRepo;
    @Autowired
    AdminService adminService;
    @Autowired
    DateRepo dateRepo;
@Autowired
AgeRepo ageRepo;

    @PostMapping("/vaccinationSlot/add")
    public String addVaccinationSlot(@RequestBody() VaccinationSlots vaccinationSlots){
        log.info("VaccinationSlotController:: /vaccinationSlot/add :"+vaccinationSlots+" called");
       if(ageRepo.existsById(vaccinationSlots.getAgeNo())==false){
           log.error("Age no incorrect : "+vaccinationSlots.getAgeNo());
           return "Age no incorrect";
       }
        if(vaccinationCentreRepo.findById(vaccinationSlots.getVaccinecentreid()).isPresent()==false){
            log.error("Vaccine Centre no is incorrect : "+vaccinationSlots.getVaccinecentreid());
            return "Vaccine Centre no is incorrect";
        }
        if(vaccineNameRepo.existsById(vaccinationSlots.getVaccineNo())==false){
            log.error("Vaccine no is incorrect : "+vaccinationSlots.getVaccineNo());
            return "Vaccine no is incorrect";
        }
        if(dateRepo.existsById(vaccinationSlots.getDateNo())==false) {
            log.error("date no is invalid : "+vaccinationSlots.getDateNo());
            return "date no is invalid";
        }
        return vaccinationSlotService.addVaccinationSlot(vaccinationSlots);

    }


    @PutMapping("vaccinationSlot/addVaccine/{vaccine_centre_id}")
    public String vaccinationCentreAddVaccine(@PathVariable(value = "vaccine_centre_id",required = false) String vaccine_centre_id,
                                              @RequestParam(value = "dose_no",required = false) String dose_no,
                                              @RequestParam(value = "vaccine_name",required = false) String vaccine_name,
                                              @RequestParam(value = "age_no",required = false)String age_no,
                                              @RequestParam(value = "date_no",required = false)String date_no,
                                              @RequestParam(value = "cnt_no",required = false) String cnt_no,
                                              @RequestHeader(value = "token",required = false) String token){
        log.info("VaccinationSlotController:: vaccinationSlot/addVaccine/{vaccine_centre_id} : { "+
                "vaccine_centre_id : " +vaccine_centre_id+
                ",dose_no : "+ dose_no+
                ",vaccine_name : "+vaccine_name+
                ",age_no : "+age_no+
                ",date_no : "+date_no+
                ",cnt_no : "+cnt_no+
                ",token : "+token
                +" } called");
        //return "id/"+id+"doseNo/"+doseNo+"vaccinneName/"+vaccineName+"ageno/"+ageNo;
        if (token == null) {
            log.error("Token Not Present in headers");
            return "Token Not Present in headers";
        }
        if(adminService.isAdminAuthenticated(token)==-1){
            log.error("Not Authenticated");
            return "Not Authenticated";
        }
        if(vaccine_centre_id==null||vaccine_centre_id==""){
            log.error("Vaccine Centre Id not present");
            return "Vaccine Centre Id not present";
        }
        if(dose_no==null||dose_no==""){
            log.error("Dose No not present");
            return "Dose No not present";
        }
        if(vaccine_name==null||vaccine_name==""){
            log.error("Vaccine Name not present");
            return "Vaccine Name not present";
        }
        if(age_no==null||age_no==""){
            log.error("Age No not present");
            return "Age No not present";
        }
        if(date_no==null||date_no==""){
            log.error("Date No not present");
            return "Date No not present";
        }
        if(cnt_no==null||cnt_no==""){
            log.error("Count no not present");
            return "Count no not present";
        }
        int vaccineCentreId=Integer.parseInt(vaccine_centre_id);
        int doseNo=Integer.parseInt(dose_no);
        int ageNo=Integer.parseInt(age_no);
        int date=Integer.parseInt(date_no);
        int cnt=Integer.parseInt(cnt_no);
       // return "";
        int vaccineNameNo;
        if(vaccineNameRepo.findByVaccineName(vaccine_name)==null){
            log.error("Vaccine Name dosent exhist: "+vaccine_name);
            return "Vaccine Name dosent exhist";
        }
        vaccineNameNo=vaccineNameRepo.findByVaccineName(vaccine_name).getId();
        if(vaccinationCentreRepo.existsById(vaccineCentreId)==false){
            log.error("Vaccination centre does not exhist: "+vaccineCentreId);
            return "Vaccination centre does not exhist";
        }
        if(dateRepo.existsById(date)==false) {
            log.error("Date No does not exhist: "+date);
            return "Date No does not exhist";
        }

        if(vaccinationSlotRepo
                .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                        vaccineCentreId,vaccineNameNo,date,ageNo)==null){
            log.error("vaccine center  not available for these entries: { "+
                    "vaccineCentreId : "+vaccineCentreId+
                    ",vaccineNameNo : "+vaccineNameNo+
                    ",date : "+date+
                    ",ageNo : "+ageNo);
            return "vaccine center  not available for these entries: { "+
                    "vaccineCentreId : "+vaccineCentreId+
                    ",vaccineNameNo : "+vaccineNameNo+
                    ",date : "+date+
                    ",ageNo : "+ageNo;
        }

       return vaccinationSlotService.addVaccine(doseNo,vaccineCentreId,vaccineNameNo,date,ageNo,cnt);


        }


}
