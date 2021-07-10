package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.CityRepo;
import com.example.covidapp.Repo.done.PincodeRepo;
import com.example.covidapp.models.done.Pincode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class PincodeService {
    @Autowired
    private PincodeRepo pincodeRepo;
    @Autowired
    private CityService cityService;
    @Autowired
    private CityRepo cityRepo;
    public int getVacciantionCountByPincode(int id) {
        Optional<Pincode> pincode=pincodeRepo.findById(id);
        if(pincode.isPresent())
            return pincode.get().getPincodeCount();
        log.error("Pincode with id :"+id+" is not present");
         return -1;
    }


    public Optional<Pincode> getPincode(int id) {

        Optional<Pincode> pincode=pincodeRepo.findById(id);
        if(pincode.isPresent())return pincodeRepo.findById(id);
        log.error("Pincode with id :"+id+" is not present");
        return null;
    }

    public String addPincode(Pincode pincode) {
       // System.out.println(pincodeRepo.findById(pincode.getPincode()));
        if(pincode==null){
            log.error("Pincode details not present");
            return "Add pincode details";
        }
        if(pincode.getPincode()==-1){
            log.error("Pincode id is missing");
            return "Add pincode value";
        }
        if((pincode.getPincode()+"").length()!=6){
            log.error("Pincode should be of 6 digits length. Entered pincode :"+pincode.getPincode());
            return "Pincode should be of 6 digits length";
        }
        if(pincode.getCityid()==-1){
            log.error("City id is missing");
            return "please enter city id";
        }
        if(cityRepo.existsById(pincode.getCityid())==false){
            log.error("City id :"+pincode.getCityid()+" does not exhist");
            return "Invalid city id";
        }
        if(pincodeRepo.existsById(pincode.getPincode())==true) {
            log.error("Pincode id :"+pincode.getPincode()+" already exhists");
            return "Pincode Already Exhists";
        }
        pincode.setPincodeCount(0);
        pincode.setSlotsCountElder(0);
        pincode.setSlotsCountYoung(0);
        pincode.setSlotsCountMinor(0);
        pincodeRepo.save(pincode);
        log.info("Pincode added successfully with info { "+pincode+" }");
        return "Pincode Added Successfully";

    }


    public String addVaccine(int id, int quant,int ageCategory) {
        Optional<Pincode> pincode=pincodeRepo.findById(id);
        if(pincode.isPresent()){
            if(ageCategory>=45)
                pincode.get().setSlotsCountElder(
                        pincode.get().getSlotsCountElder()+quant
                );
            else if(ageCategory>=18)
                pincode.get().setSlotsCountYoung(
                        pincode.get().getSlotsCountYoung()+quant
                );
            else{
                pincode.get().setSlotsCountMinor(
                        pincode.get().getSlotsCountMinor()+quant
                );}
            pincodeRepo.save(pincode.get());

            cityService.addVaccine(pincode.get().getCityid(),quant,ageCategory);
            log.info(quant+" vaccines added successfully to Pincode id :"+id);
            return "Vaccines Added Successfully";
        }
        log.error("Pincode id :"+id+" does not exhist");
        return "Pincode Not Found";
    }


    public String addVaccinatedCount(int pincodeid, int cnt) {
        Optional<Pincode> pincode=pincodeRepo.findById(pincodeid);
        if(pincode.isPresent()){
            pincode.get().setPincodeCount(
                    pincode.get().getPincodeCount()+cnt
            );
            pincodeRepo.save(pincode.get());

            cityService.addVaccinatedCount(pincode.get().getCityid(),cnt);
            log.info(cnt+" people added to vaccinated count for pincode :"+pincodeid);
            return "Vaccinated count added successfully";
        }
        log.error("Pincode not present with id :"+pincodeid);
        return "Pincode Not Found";
    }
}
