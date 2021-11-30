package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.CountryRepo;
import com.example.covidapp.models.done.Country;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Log4j2
public class CountryService {
    @Autowired
    private CountryRepo countryRepo;

    public int getVacciantionCountByCountryId(int id) {
        Optional<Country> country=countryRepo.findById(id);
        if(country.isPresent())
            return country.get().getCountryCount();
        log.error("Country not present with id :"+id);
         return -1;
    }

    public int getVacciantionCountByCountryName(String name) {
        Country country=countryRepo.findByCountryName(name);
        if (country!=null)
        return country.getCountryCount();
        log.error("Country not present with name :"+name);
        return -1;
    }

    public Optional<Country> getCountryById(int id) {

        Optional<Country> country=countryRepo.findById(id);
        if(country.isPresent())
            return countryRepo.findById(id);
        log.error("Country not present with id :"+id);
        return null;
    }

    public Country getCountryByName(String name) {
        Country country=countryRepo.findByCountryName(name);
        if (country!=null)
            return countryRepo.findByCountryName(name);
        log.error("Country not present with name :"+name);
        return null;

    }

    public String addVaccinatedCount(int countryid, int cnt) {
        Optional<Country> country=countryRepo.findById(countryid);
        if(country.isPresent()){
            country.get().setCountryCount(
                    country.get().getCountryCount()+cnt
            );
            countryRepo.save(country.get());
            log.info(cnt+" people added to vaccinated count for country id :"+countryid);
            return "Vaccinated count added successfully";
        }
        log.error("Country not present with id :"+countryid);
        return "Country Not Found";
    }
}
