package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.StateRepo;
import com.example.covidapp.models.done.State;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Log4j2
public class StateService {
    @Autowired
    private StateRepo stateRepo;
    @Autowired
    CountryService countryService;

    public int getVacciantionCountByStateId(int id) {
        Optional<State> state=stateRepo.findById(id);
        if(state.isPresent())
            return state.get().getStateCount();
        log.error("State with id :"+id+" is not present");
        return -1;
    }

    public int getVacciantionCountByStateName(String name) {
        State state=stateRepo.findByStateName(name);
        if (state!=null)
            return state.getStateCount();
        log.error("State with name :"+name+" is not present");
        return -1;
    }

    public Optional<State> getStateById(int id) {
        Optional<State> state=stateRepo.findById(id);
        if(state.isPresent())
            return stateRepo.findById(id);
        log.error("State with id :"+id+" is not present");
        return null;
    }

    public State getStateByName(String name) {
//        System.out.println(stateRepo.findByStateName(name).getCountry()+"..................."
//               );
        State state=stateRepo.findByStateName(name);
        if (state!=null)
            return stateRepo.findByStateName(name);
        log.error("State with name :"+name+" is not present");
        return null;

    }

    public String addVaccinatedCount(int stateid, int cnt) {
        Optional<State> state=stateRepo.findById(stateid);
        if(state.isPresent()){
            state.get().setStateCount(
                    state.get().getStateCount()+cnt
            );
            countryService.addVaccinatedCount(state.get().getCountryid(),cnt);
            stateRepo.save(state.get());
            log.info(cnt+" people added to vaccinated count for state id :"+state);
            return "Vaccinated count added successfully";
        }
        log.error("State not present with id :"+stateid);
        return "State Not Found";
    }
}
