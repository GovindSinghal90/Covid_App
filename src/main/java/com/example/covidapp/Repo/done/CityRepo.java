package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepo extends JpaRepository<City,Integer> {
    City findByCityName(String name);
    City findByCityNameAndStateid(String a,int b);
}
