package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.DateRepo;
import com.example.covidapp.Repo.done.PincodeRepo;
import com.example.covidapp.Repo.done.VaccinationCentreRepo;
import com.example.covidapp.Repo.done.VaccineNameRepo;
import com.example.covidapp.Repo.done.VaccinationSlotRepo;
import com.example.covidapp.kafka2.VaccineCentreKafka;
import com.example.covidapp.kafka2.VaccineSlotKafka;
import com.example.covidapp.models.done.VaccinationCentre;
import com.example.covidapp.models.done.VaccineName;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class VaccinationCentreService {
    @Autowired
    private VaccinationCentreRepo vaccinationCentreRepo;
    @Autowired
    private PincodeService pincodeService;
@Autowired
private PincodeRepo pincodeRepo;
@Autowired
private VaccinationSlotRepo vaccinationSlotRepo;
@Autowired
private VaccineNameRepo vaccineNameRepo;
@Autowired
private DateRepo dateRepo;
@Autowired
private VaccineCentreKafka vaccineCentreKafka;
@Autowired
private VaccineSlotKafka vaccineSlotKafka;
    public int getVacciantionCountByVaccinationCentreId(int id) {
        Optional<VaccinationCentre> vaccinationCentre=vaccinationCentreRepo.findById(id);
        if(vaccinationCentre.isPresent())
            return vaccinationCentre.get().getVaccinationCentreCount();
        log.error("VaccinationCentre with id :"+id+" is not present");
        return -1;
    }

    public Optional<VaccinationCentre> getVaccinationCentreById(int id) {
        Optional<VaccinationCentre> vaccinationCentre=vaccinationCentreRepo.findById(id);
        if(vaccinationCentre.isPresent())
            return vaccinationCentreRepo.findById(id);
        log.error("VaccinationCentre with id :"+id+" is not present");
        return null;
    }


    public String addVaccinationCentre(VaccinationCentre vaccinationCentre) {
        if(vaccinationCentre==null){
            log.error("VaccinationCentre details not present");
            return "Add vaccination centre details";
        }
        if(vaccinationCentre.getVaccinationCentreAddress().equals("")){
            log.error("VaccinationCentre details is Empty");
            return "add vaccination centre address";
        }
        if(vaccinationCentre.getPincodeid()==-1){
            log.error("Pincode id is not present");
            return "add pincode id";
        }
        if(pincodeRepo.existsById(vaccinationCentre.getPincodeid())==false){
            log.error("No pincode exhists with this id :"+vaccinationCentre.getPincodeid());
            return "No pincode exhists with this id";
        }
        System.out.println(vaccinationCentre.getId());
        VaccinationCentre v=vaccinationCentreRepo
                .findByVaccinationCentreAddressAndPincodeid(vaccinationCentre.getVaccinationCentreAddress(),vaccinationCentre.getPincodeid());

        if(v==null||v.getPincodeid()!=vaccinationCentre.getPincodeid()){
            vaccinationCentre.setVaccinationCentreCount(0);
            vaccinationCentre.setSlotsCountElder(0);
            vaccinationCentre.setSlotsCountMinor(0);
            vaccinationCentre.setSlotsCountYoung(0);
            vaccinationCentreRepo.save(vaccinationCentre);
            addVaccinationSlotsOnVaccinationCentreCreation(vaccinationCentre.getId());
            log.info("Vaccination centre :"+vaccinationCentre+" added successfully");
            vaccineCentreKafka.saveCreateVaccineCentreLog(vaccinationCentre);
            return "Vaccination Centre Added Successfully";
        }
        log.error("Vaccination Centre with details { "+vaccinationCentre+" }Already Exhists");
        return "Vaccination Centre Already Exhists";


    }

    public String addVaccine(int id, int quant,int ageCategory) {
        Optional<VaccinationCentre> vaccinationCentre=vaccinationCentreRepo.findById(id);
        if(vaccinationCentre.isPresent()){
           if(ageCategory>=45)
               vaccinationCentre.get().setSlotsCountElder(
                       vaccinationCentre.get().getSlotsCountElder()+quant
               );
           else if(ageCategory>=18)
               vaccinationCentre.get().setSlotsCountYoung(
                       vaccinationCentre.get().getSlotsCountYoung()+quant
               );
           else{
               vaccinationCentre.get().setSlotsCountMinor(
                       vaccinationCentre.get().getSlotsCountMinor()+quant
               );}
           vaccinationCentreRepo.save(vaccinationCentre.get());
           pincodeService.addVaccine(vaccinationCentre.get().getPincodeid(),quant,ageCategory);
            log.info(quant+" vaccines added successfully to Vaccination Centre id :"+id);
            vaccineCentreKafka.saveCreateVaccineCentreLog(vaccinationCentre.get());
            return "Vaccines Added Successfully";
        }
        log.error("Vaccination Centre with id :"+id+" not found");
        return "Vaccnation Centre Not Found";
    }
    public String addVaccinatedCount(int index,int cnt){
        Optional<VaccinationCentre> vaccinationCentre=vaccinationCentreRepo.findById(index);
        if(vaccinationCentre.isPresent()){
           vaccinationCentre.get().setVaccinationCentreCount(
                   vaccinationCentre.get().getVaccinationCentreCount()+cnt
           );
            vaccinationCentreRepo.save(vaccinationCentre.get());
            pincodeService.addVaccinatedCount(vaccinationCentre.get().getPincodeid(),cnt);
            log.info(cnt+" people added to vaccinated count for Vaccination Centre id :"+index);
            vaccineCentreKafka.saveCreateVaccineCentreLog(vaccinationCentre.get());
            return "Vaccinated count added successfully";
        }
        log.error("Vaccination Centre with id :"+index+" not found");
        return "Vaccnation Centre Not Found";
    }
    public void addVaccinationSlotsOnVaccinationCentreCreation(int vaccinationCentreId){
        int dates=(dateRepo.findAll()).size();
        List<VaccineName> lst=vaccineNameRepo.findAll();
        int[]ageArr=new int[3];
        ageArr[0]=10;
        ageArr[1]=20;
        ageArr[2]=60;
        for(int i=1;i<=dates;i++){
            for(int j=0;j<3;j++){
                for(int k=0;k<lst.size();k++){
                    VaccinationSlots vaccinationSlots=new VaccinationSlots();
                    vaccinationSlots.setDateNo(i);
                    vaccinationSlots.setAgeNo(ageArr[j]);
                    vaccinationSlots.setVaccineNo(lst.get(k).getId());
                    vaccinationSlots.setVaccinecentreid(vaccinationCentreId);
                    vaccinationSlotRepo.save(vaccinationSlots);
                    vaccineSlotKafka.saveCreateVaccineSlotLog(vaccinationSlots);
                }
            }
        }
        log.info("vaccination slots added on vaccination centre creation");
        System.out.println(dates+".........................////////////");
    }
}
