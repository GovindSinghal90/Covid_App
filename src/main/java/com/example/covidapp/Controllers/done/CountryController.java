package com.example.covidapp.Controllers.done;

import com.example.covidapp.Services.done.CountryService;
import com.example.covidapp.models.done.Country;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Log4j2
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("country/vaccinatedCount/id")
    public int getVacciantionCountByCountryId(@RequestParam(defaultValue = "-1") int id){
        log.info( "[ "+id+" called ]");
        if(id==-1){
            log.error("country id not present");
            return -1;}
        return countryService.getVacciantionCountByCountryId(id);
    }

    @GetMapping("country/vaccinatedCount/name")
    public int getVacciantionCountByCountryName(@RequestParam(defaultValue = "") String name){
        log.info("[ "+name+" called ]");

        if(name.equals("")){
            log.error("country name not present");
            return -1;}
        return countryService.getVacciantionCountByCountryName(name);
    }

    @GetMapping("country/id")
    public Optional<Country> getCountryById(@RequestParam(defaultValue = "-1") int id){
        log.info("[ "+id+" called ]");
        if(id==-1){
            log.error("country id not present");
            return null;}
        return countryService.getCountryById(id);
    }

    @GetMapping("country/name")
    public Country getCountryByName(@RequestParam(defaultValue = "") String name){
        log.info("[ "+name+" called ]");
        if(name==""){
            log.error("country name not present");
            return null;}
         return countryService.getCountryByName(name);
    }

}
