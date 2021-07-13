package com.example.covidapp.Controllers.done;


import com.example.covidapp.Services.done.AdminService;
import com.example.covidapp.Services.done.PincodeService;
import com.example.covidapp.models.done.Pincode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Log4j2
public class PincodeController {

    @Autowired
    private PincodeService pincodeService;
    @Autowired
    private AdminService adminService;

    @GetMapping("pincode/vaccinatedCount")
    public int getVacciantionCountByPincode(@RequestParam(defaultValue = "-1") int id){
        log.info("Pincode:: pincode/vaccinatedCount called with id: "+id);
        if(id==-1){
            log.error("Pincode id not present");
            return -1;
        }
        return pincodeService.getVacciantionCountByPincode(id);
    }

    @GetMapping("pincode")
    public Optional<Pincode> getPincode(@RequestParam(defaultValue = "-1") int id){
        log.info("Pincode:: /pincode called with id: "+id);
        if(id==-1){
            log.error("Pincode id not present");
            return null;
        }
        System.out.println(id+"");
        return pincodeService.getPincode(id);
    }

    @PostMapping("pincode/add")
    public String addPincode(@RequestBody(required = false) Pincode pincode,
                             @RequestHeader(value = "token",required = false) String token){
        log.info("Pincode:: pincode/add called with pincode :"+pincode+" and token :"+token);
        if (token == null) {
            log.error("Token Not Present in headers");
            return "Token Not Present in headers";
        }
        if(adminService.isAdminAuthenticated(token)==-1){
            log.error("Not Authenticated");
            return "Not Authenticated";
        }
        else {
            System.out.println(pincode + ".............");
            try {
                return pincodeService.addPincode(pincode);

            } catch (Exception e) {
                log.error("Error occured"+e);
                return e.toString();
            }
        }
    }

//    @PutMapping("pincode/addVaccine/{id}")
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
//            return pincodeService.addVaccine(id, quant, ageCategory);
//        }
//    }

}

