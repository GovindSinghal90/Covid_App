package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.VaccineName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineNameRepo extends JpaRepository<VaccineName,Integer> {
    VaccineName findByVaccineName(String s);

}
