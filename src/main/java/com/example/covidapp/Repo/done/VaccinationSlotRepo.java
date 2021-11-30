package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.VaccinationSlots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinationSlotRepo extends JpaRepository<VaccinationSlots,Integer> {
    VaccinationSlots findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(int a,int b,int c,int d);
List<VaccinationSlots> findAllByDateNo(int i);
VaccinationSlots findByVaccinecentreidAndDateNo(int a,int b);
void deleteAllByDateNo(int i);
}
