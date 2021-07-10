package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationCentreRepo extends JpaRepository<VaccinationCentre,Integer> {
VaccinationCentre findByVaccinationCentreAddress(String address);
VaccinationCentre findByVaccinationCentreAddressAndPincodeid(String a,int b);
}
