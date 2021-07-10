package com.example.covidapp.Controllers.todo;

import com.example.covidapp.Services.done.AdminService;
import com.example.covidapp.Services.done.CityService;
import com.example.covidapp.models.done.City;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@Log4j2
public class CityController {

    @Autowired
    private CityService cityService;
    @Autowired
    private AdminService adminService;

    @GetMapping("city/vaccinatedCount/id")
    public int getVacciantionCountByCityId(@RequestParam(defaultValue = "-1") int id){
        log.info("CityController:: city/vaccinatedCount/id called with id: "+id);
        if(id==-1){
            log.error("City id not present");
            return -1;
        }
        return cityService.getVacciantionCountByCityId(id);
    }

    @GetMapping("city/vaccinatedCount/name")
    public int getVacciantionCountByCityName(@RequestParam(defaultValue = "") String name){
        log.info("CityController:: city/vaccinatedCount/name called with name: "+name);
        if(name==""){
            log.error("City name not present");
            return -1;
        }
        return cityService.getVacciantionCountByCityName(name);
    }

    @GetMapping("city/id")
    public Optional<City> getCityById(@RequestParam(defaultValue = "-1") int id){
        log.info("CityController:: city/id called with id: "+id);
        if(id==-1){
            log.error("City id not present");
            return null;
        }
        return cityService.getCityById(id);
    }

    @GetMapping("city/name")
    public City getCityByName(@RequestParam(defaultValue = "") String name){
        log.info("CityController:: city/vaccinatedCount/name called with name: "+name);
        if(name==""){
            log.error("City name not present");
            return null;
        }
        return cityService.getCityByName(name);
    }

    @PostMapping("city/add")
    public String addCity(@RequestBody(required = false) City city,
                          @RequestHeader(value = "token",required = false) String token){
        log.info("CityController:: city/add called with city: "+city+" and token: "+token);
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
                String s = cityService.addCity(city);
                return s;
            } catch (Exception e) {
                log.error("Error occured"+e);
                return e.toString();
            }
        }
    }

//    @PutMapping("city/addVaccine/{id}")
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
//            return cityService.addVaccine(id, quant, ageCategory);
//        }
//    }
}
