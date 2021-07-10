package com.example.covidapp.Controllers.done;


import com.example.covidapp.Services.done.AdminService;
import com.example.covidapp.Services.done.VaccinationCentreService;
import com.example.covidapp.models.done.VaccinationCentre;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Log4j2
public class VaccinationCentreController {

    @Autowired
    private VaccinationCentreService vaccinationCentreService;
    @Autowired
    private AdminService adminService;


    @GetMapping("vaccinationCentre/vaccinatedCount/id")
    public int getVacciantionCountByVaccinationCentreId(@RequestParam(defaultValue = "-1") int id){
        log.info("VaccinationCentreController:: vaccinationCentre/vaccinatedCount/id called with id: "+id);
        if(id==-1){
            log.error("Vaccination Centre id not present");
            return -1;
        }
        return vaccinationCentreService.getVacciantionCountByVaccinationCentreId(id);
    }

    @GetMapping("vaccinationCentre/id")
    public Optional<VaccinationCentre> getVaccinationCentreById(@RequestParam(defaultValue = "-1") int id){
        log.info("VaccinationCentreController:: vaccinationCentre/id called with id: "+id);
        if(id==-1){
            log.error("Vaccination Centre id not present");
            return null;
        }
        return vaccinationCentreService.getVaccinationCentreById(id);
    }


    @PostMapping("vaccinationCentre/add")
    public String addVaccinationCentre(@RequestBody(required = false) VaccinationCentre vaccinationCentre,
                                       @RequestHeader(value = "token",required = false) String token){
        log.info("VaccinationCentreController:: vaccinationCentre/add called with Vaccination Centre { "
        +vaccinationCentre+" }+ and token :"+token);
        if (token == null) {
            log.error("Token Not Present in headers");
            return "Token Not Present in headers";
        }
        if(adminService.isAdminAuthenticated(token)==-1){
            log.error("Not Authenticated");
            return "Not Authenticated";
        }
        else {
            try {
                String s = vaccinationCentreService.addVaccinationCentre(vaccinationCentre);
                return s;
            } catch (Exception e) {
                log.error("Error Occured : "+e);
                return e.toString();
            }
        }
    }
//    @PutMapping("vaccinationCentre/addVaccine/{id}")
//    public String vaccinationCentreAddVaccine(@PathVariable("id") int id,
//                                              @RequestParam("vaccine_quant") int quant,
//                                              @RequestParam("vaccine_name") String name,
//                                              @RequestParam("age_category")int ageCategory,
//                                              @RequestHeader(value = "token",required = false) String token){
//        if (token == null) {
//            return "Token Not Present in headers";
//        }
//        if(adminService.isAdminAuthenticated(token)==-1){
//            return "Not Authenticated";
//        }
//        else {
//            return vaccinationCentreService.addVaccine(id, quant, ageCategory);
//        }
//    }

}
