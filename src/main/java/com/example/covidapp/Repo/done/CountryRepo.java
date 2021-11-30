package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepo extends JpaRepository<Country,Integer> {
    Country findByCountryName(String name);
}
