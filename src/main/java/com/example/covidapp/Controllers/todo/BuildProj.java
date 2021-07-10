package com.example.covidapp.Controllers.todo;

import com.example.covidapp.Repo.done.*;
import com.example.covidapp.Repo.done.VaccinationSlotRepo;
import com.example.covidapp.Services.done.VaccinationCentreService;
import com.example.covidapp.kafka2.VaccineCentreKafka;
import com.example.covidapp.kafka2.VaccineNameKafka;
import com.example.covidapp.models.done.*;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Log4j2
public class BuildProj {
    @Autowired
    private CountryRepo countryRepo;
    @Autowired
    private StateRepo stateRepo;
    @Autowired
    private CityRepo cityRepo;
    @Autowired
    private PincodeRepo pincodeRepo;
    @Autowired
    private VaccinationCentreRepo vaccinationCentreRepo;
    @Autowired
    private VaccinationSlotRepo vaccinationSlotRepo;
    @Autowired
    private AgeRepo ageRepo;
    @Autowired
    private DateRepo dateRepo;
    @Autowired
    private VaccineNameRepo vaccineNameRepo;
    @Autowired
    private VaccinationCentreService vaccinationCentreService;
    @Autowired
    private RegisteredCustomerRepo registeredCustomerRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private VaccineNameKafka vaccineNameKafka;
    @Autowired
    private VaccineCentreKafka vaccineCentreKafka;
    @PostConstruct
    public void addData(){
                //add dates

        Date date1=new Date();
        date1.setId(1);
        date1.setMyDate(java.time.LocalDate.now()+"");
        Date date2=new Date();
        date2.setId(2);
        date2.setMyDate(java.time.LocalDate.now().plusDays(1)+"");
        dateRepo.save(date1);
        dateRepo.save(date2);

        //add vaccine names
        VaccineName v1=new VaccineName();
        VaccineName v2=new VaccineName();
        v1.setVaccineName("Covishield");
        v2.setVaccineName("Covaxin");
        if(vaccineNameRepo.findByVaccineName("Covishield")==null){
            vaccineNameRepo.save(v1);
            vaccineNameKafka.saveCreateVaccineNameLog(v1);
        }

        if(vaccineNameRepo.findByVaccineName("Covaxin")==null) {
            vaccineNameRepo.save(v2);
            vaccineNameKafka.saveCreateVaccineNameLog(v2);
        }


        //add age
        Age age1=new Age();
        Age age2=new Age();
        Age age3=new Age();
        age1.setAge(10);
        age1.setCategory("Minor");
        age2.setAge(20);
        age2.setCategory("Young");
        age3.setAge(60);
        age3.setCategory("Elder");

        ageRepo.save(age1);
        ageRepo.save(age2);
        ageRepo.save(age3);


        //add country
        Country country=new Country();
        country.setId(1);
        country.setCountryName("India");
      if(countryRepo.findByCountryName("India")==null)
        countryRepo.save(country);

        //add states
        State state=new State();
        state.setId(1);
        state.setStateName("UP");
        state.setCountryid(1);
        if(stateRepo.findByStateName("UP")==null)
        stateRepo.save(state);


        //add city
        City city1=new City();
        City city2=new City();
        city1.setCityName("Lucknow");
        city1.setStateid(stateRepo.findByStateName("UP").getId());
        city2.setCityName("Agra");
        city2.setStateid(stateRepo.findByStateName("UP").getId());
        if(cityRepo.findByCityNameAndStateid("Lucknow", stateRepo.findByStateName("UP").getId())==null)
        cityRepo.save(city1);
        if(cityRepo.findByCityNameAndStateid("Agra", stateRepo.findByStateName("UP").getId())==null)
        cityRepo.save(city2);

       // add pincode
        Pincode pincode1=new Pincode();
        Pincode pincode2=new Pincode();
        pincode1.setPincode(208027);
        pincode1.setCityid(cityRepo.findByCityName("Lucknow").getId());
        pincode2.setPincode(208028);
        pincode2.setCityid(cityRepo.findByCityName("Lucknow").getId());

        pincodeRepo.save(pincode1);
        pincodeRepo.save(pincode2);


        //add vaccination centre
        VaccinationCentre vaccinationCentre1=new VaccinationCentre();
        VaccinationCentre vaccinationCentre2=new VaccinationCentre();
        vaccinationCentre1.setVaccinationCentreAddress("civil lines");
        vaccinationCentre1.setPincodeid(208027);
        vaccinationCentre2.setVaccinationCentreAddress("chandani chowk");
        vaccinationCentre2.setPincodeid(208027);
      if(vaccinationCentreRepo.findByVaccinationCentreAddressAndPincodeid("civil lines",208027)==null)
      {vaccinationCentreRepo.save(vaccinationCentre1);
          vaccineCentreKafka.saveCreateVaccineCentreLog(vaccinationCentre1);
          vaccinationCentreService.addVaccinationSlotsOnVaccinationCentreCreation(vaccinationCentre1.getId());
      }
        if(vaccinationCentreRepo.findByVaccinationCentreAddressAndPincodeid("chandani chowk",208027)==null){
            vaccinationCentreRepo.save(vaccinationCentre2);
            vaccineCentreKafka.saveCreateVaccineCentreLog(vaccinationCentre1);
            vaccinationCentreService.addVaccinationSlotsOnVaccinationCentreCreation(vaccinationCentre2.getId());
        }
//        List<RegisteredCustomer>lst=registeredCustomerRepo.findAll();
//        for(int i=0;i<lst.size();i++){
//            if(lst.get(i).getVaccinationSlots().)
//            System.out.println(lst.get(i).getPhNo()+" "+lst.get(i).getVaccinationSlotId()+"   "+lst.get(i).getVaccinationSlots());
//        }

        //add vaccination slot
        //automatically gets created
    }
    @Scheduled(zone = "GMT+5:30",cron = "0 0 23 * * ?")
    public void ataspecificTime(){
        System.out.println("YES");

        //set all customers from registered to unregistered
        List<RegisteredCustomer> lstRegisterdCustomer=registeredCustomerRepo.findAll();
        for (int i=0;i<lstRegisterdCustomer.size();i++){
            if(lstRegisterdCustomer.get(i).getVaccinationSlots().getDateNo()==1){
             Customer customer=customerRepo.findById(lstRegisterdCustomer.get(i).getPhNo()).get();
             customer.setVaccinationStatus(customer.getVaccinationStatus()-1);
             customerRepo.save(customer);
            }
        }
        log.info("all customers converted from registered to unregistered");


        //add todays vaccines to tomorrows
        List<VaccinationSlots> lstVaccinationSlot=vaccinationSlotRepo.findAllByDateNo(1);
        for (int i=0;i<lstVaccinationSlot.size();i++){
            VaccinationSlots vaccinationSlotPrev=lstVaccinationSlot.get(i);
            VaccinationSlots vaccinationSlotsNew=vaccinationSlotRepo
                    .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                    vaccinationSlotPrev.getVaccinecentreid(),
                            vaccinationSlotPrev.getVaccineNo(),
                            2,
                            vaccinationSlotPrev.getAgeNo()
            );
            vaccinationSlotsNew.setDoseNo1(vaccinationSlotsNew.getDoseNo1()+vaccinationSlotPrev.getDoseNo1());
            vaccinationSlotsNew.setDoseNo2(vaccinationSlotsNew.getDoseNo2()+vaccinationSlotPrev.getDoseNo2());
            vaccinationSlotRepo.save(vaccinationSlotsNew);

        }
        log.info("todays vaccines added to tomorrows");



        //remove all registered customer slots with todays booking
        for(int i=0;i<lstRegisterdCustomer.size();i++){
            if(lstRegisterdCustomer.get(i).getVaccinationSlots().getDateNo()==1){
                registeredCustomerRepo.deleteById(lstRegisterdCustomer.get(i).getPhNo());
            }
        }
        log.info("removed all registered customer slots with todays booking");

        //remove all vaccinationslots with todays date
        vaccinationSlotRepo.deleteAllByDateNo(1);
        log.info("removed all vaccinationslots with todays date");

        //set all dates again
        int size=dateRepo.findAll().size();
        for(int i=1;i<=size;i++){
            Date date=dateRepo.findById(i).get();
            date.setMyDate(java.time.LocalDate.now().plusDays(i-1)+"");
            dateRepo.save(date);
        }
        log.info("all dates resetted");

        //update all vaccination slots date to -1<-1st sort by date;
        List<VaccinationSlots>lstVaccinationSlotsAll=vaccinationSlotRepo.findAll();
        Collections.sort(lstVaccinationSlotsAll, new Comparator<VaccinationSlots>(){
            public int compare(VaccinationSlots o1,VaccinationSlots o2){
                return o1.getDateNo() - o2.getDateNo();
            }
        });
        for(int i=0;i<lstVaccinationSlotsAll.size();i++){
            VaccinationSlots vaccinationSlots=lstVaccinationSlotsAll.get(i);
            vaccinationSlots.setDateNo(vaccinationSlots.getDateNo()-1);
            vaccinationSlotRepo.save(vaccinationSlots);
        }
        log.info("updated all vaccination slots date to -1<-1st sort by date");

        //add vaccination slots for last date
        List<VaccinationCentre>lstVaccineCentre=vaccinationCentreRepo.findAll();
        List<VaccineName> lst=vaccineNameRepo.findAll();
        int[]ageArr=new int[3];
        ageArr[0]=10;
        ageArr[1]=20;
        ageArr[2]=60;
        for(int i=0;i<lstVaccineCentre.size();i++){
            for(int j=0;j<3;j++){
                for(int k=0;k<lst.size();k++){
                    VaccinationSlots vaccinationSlots=new VaccinationSlots();
                    vaccinationSlots.setDateNo(size);
                    vaccinationSlots.setAgeNo(ageArr[j]);
                    vaccinationSlots.setVaccineNo(lst.get(k).getId());
                    vaccinationSlots.setVaccinecentreid(lstVaccineCentre.get(i).getId());
                    vaccinationSlotRepo.save(vaccinationSlots);
                }
            }
        }
        log.info("added vaccination slots for last date");


    }

}
