package com.example.covidapp.Controllers.done;

import com.example.covidapp.Services.done.StateService;
import com.example.covidapp.models.done.State;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Log4j2
public class StateController {
    @Autowired
    private StateService stateService;

    @GetMapping("state/vaccinatedCount/id")
    public int getVacciantionCountByStateId(@RequestParam(defaultValue = "-1") int id){
        log.info("StateController:: state/vaccinatedCount/id called with id: "+id);
        if(id==-1){
            log.error("State id not present");
            return -1;}
        return stateService.getVacciantionCountByStateId(id);
    }

    @GetMapping("state/vaccinatedCount/name")
    public int getVacciantionCountByStateName(@RequestParam(defaultValue = "") String name){
        log.info("StateController:: state/vaccinatedCount/name called with name: "+name);
        if(name==""){
            log.error("State name not present");
            return -1;}
        return stateService.getVacciantionCountByStateName(name);
    }

    @GetMapping("state/id")
    public Optional<State> getStateById(@RequestParam(defaultValue = "-1") int id){
        log.info("StateController:: state/id called with id: "+id);
        if(id==-1){
            log.error("State id not present");
            return null;}
        return stateService.getStateById(id);
    }

    @GetMapping("state/name")
    public State getStateByName(@RequestParam(defaultValue = "") String name){
        log.info("StateController:: state/name called with name: "+name);
        if(name==""){
            log.error("State name not present");
            return null;}
        State st=stateService.getStateByName(name);
         return st;
    }

}
