package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.CityRepo;
import com.example.covidapp.Repo.done.StateRepo;
import com.example.covidapp.models.done.City;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class CityService {
    @Autowired
    private CityRepo cityRepo;
@Autowired
private StateService stateService;
@Autowired
private StateRepo stateRepo;
    public int getVacciantionCountByCityId(int id) {
        Optional<City> city=cityRepo.findById(id);
        if(city.isPresent())
            return city.get().getCityCount();
        log.error("City with id :"+id+" is not present");
         return -1;
    }

    public int getVacciantionCountByCityName(String name) {
        City city=cityRepo.findByCityName(name);
        if (city!=null)
            return city.getCityCount();
        log.error("City with name :"+name+" is not present");
        return -1;
    }

    public Optional<City> getCityById(int id) {
        Optional<City> city=cityRepo.findById(id);
        if(city.isPresent())
            return cityRepo.findById(id);
        log.error("City with id :"+id+" is not present");
        return null;
    }

    public City getCityByName(String name) {
        City city=cityRepo.findByCityName(name);
        if (city!=null)
            return cityRepo.findByCityName(name);
        log.error("City with name :"+name+" is not present");
        return null;

    }

    public String addCity(City city) {
        if(city==null){
            log.error("city details not passed");
            return "Empty City details";
        }
        if(city.getCityName()==null||city.getCityName()==""){
            log.error("city name not passed");
            return "Please fill city name";
        }
        if(city.getStateid()==-1){
            log.error("state id not passed");
            return "Please fill state id";
        }
        if(stateRepo.existsById(city.getStateid())==false){
            log.error("No state exhists with state id :"+city.getStateid());
            return "No state exhists with this state id";
        }
        City v=cityRepo.findByCityNameAndStateid(city.getCityName(),city.getStateid());

        if(v==null){
            city.setCityCount(0);
            city.setSlotsCountElder(0);
            city.setSlotsCountMinor(0);
            city.setSlotsCountElder(0);
            cityRepo.save(city);
            log.info("City {"+v+"} Added Successfully");
            return "City Added Successfully";
        }
        log.error("City Already Exhists with city name :"+city.getCityName()+" and state id :"+city.getStateid());
        return "City Already Exhists";

    }

    public String addVaccine(int id, int quant,int ageCategory) {
        Optional<City> city=cityRepo.findById(id);
        if(city.isPresent()){
            if(ageCategory>=45)
                city.get().setSlotsCountElder(
                        city.get().getSlotsCountElder()+quant
                );
            else if(ageCategory>=18)
                city.get().setSlotsCountYoung(
                        city.get().getSlotsCountYoung()+quant
                );
            else{
                city.get().setSlotsCountMinor(
                        city.get().getSlotsCountMinor()+quant
                );}
            cityRepo.save(city.get());
            log.info(quant+" vaccines added successfully to City id :"+id);
            return "Vaccines Added Successfully";
        }
        log.error("City not found for id :"+id);
        return "City Not Found";
    }

    public String addVaccinatedCount(int cityid, int cnt) {
        Optional<City> city=cityRepo.findById(cityid);
        if(city.isPresent()){
           city.get().setCityCount(
                   city.get().getCityCount()+cnt
           );System.out.println("hh");
            stateService.addVaccinatedCount(city.get().getStateid(),cnt);
            System.out.println("hh");
            cityRepo.save(city.get());
            System.out.println("hh");
            log.info(cnt+" people added to vaccinated count for city id :"+cityid);
            return "Vaccinated count added successfully";
        }
        log.error("City not present with id :"+cityid);
        return "City Not Found";
    }
}
