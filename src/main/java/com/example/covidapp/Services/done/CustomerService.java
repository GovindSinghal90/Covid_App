package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.CustomerRepo;
import com.example.covidapp.Repo.done.CustomerTokenRepo;
import com.example.covidapp.Repo.done.RegisteredCustomerRepo;
import com.example.covidapp.Repo.done.VaccinationSlotRepo;
import com.example.covidapp.models.done.Customer;
import com.example.covidapp.models.done.RegisteredCustomer;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CustomerService {

    @Autowired
   private CustomerRepo customerRepo;
    @Autowired
    private CustomerTokenService tokenService;
    @Autowired
    private RegisteredCustomerRepo registeredCustomerRepo;
@Autowired
private CustomerTokenRepo customerTokenRepo;
@Autowired
private VaccinationSlotRepo vaccinationSlotRepo;
@Autowired
private VaccinationSlotService vaccinationSlotService;

    public String registerCustomer(Customer customer) {
        if(customer.getFirstName()==null||customer.getLastName()==null||customer.getAge()<=0||
        customer.getFirstName()==""||customer.getLastName()=="") {
            log.error("Incomplete Details in customer : "+customer);
            return "Incomplete Details";
        }
        if((customer.getPhoneNumber()+"").toString().length()!=10) {
            log.error("Phone Number Should be of 10 digits : "+customer.getPhoneNumber());
            return "Phone Number Should be of 10 digits";
        }
        if(customerRepo.findById(customer.getPhoneNumber()).isPresent()) {
            log.error("Customer Exhists With this Phone Number : "+customer.getPhoneNumber());
            return "Customer Exhists With this Phone Number";
        }
        try{customer.setVaccinationStatus(0);
            customer.setMyVaccinationDate("");
            customer.setVaccinenameid(0);
            customerRepo.save(customer);
            return tokenService.addTokken(customer.getPhoneNumber());

        }
        catch (Exception e){
            log.error("Error Occured : "+e);
            return e.getMessage();
        }

    }

    public String logoutCustomer(String token) {
        return tokenService.removeToken(token);
    }

    public String loginCustomer(long phoneNumber) {
        if((phoneNumber+"").toString().length()!=10) {
            log.error("Phone Number Should be of 10 digits : "+phoneNumber);
            return "Phone Number Should be of 10 digits";
        }
        if(customerRepo.existsById(phoneNumber)){
           return tokenService.addTokken(phoneNumber);
        }
        else {
            log.error("No Account With this phone number : "+phoneNumber);
            return "No Account With this phone number... PLZZ LOGIN...";
        }
    }

    public Long isCustomerAuthenticated(String token){
        return tokenService.isTokenPresent(token);
    }


    public String registerForVaccine(String token, int vaccineCentreId, int vaccineNameNo, int date, int ageNo,int doseNo) {

        RegisteredCustomer registeredCustomer=new RegisteredCustomer();
        registeredCustomer.setPhNo(customerTokenRepo.findById(token).get().getPhoneNumber());

        VaccinationSlots vaccinationSlots=vaccinationSlotRepo
                .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                        vaccineCentreId,vaccineNameNo,date,ageNo);
        registeredCustomer.setVaccinationSlotId(vaccinationSlots.getId());

        registeredCustomerRepo.save(registeredCustomer);

        vaccinationSlotService.addVaccine(doseNo,vaccineCentreId,vaccineNameNo,date,ageNo,-1);

        System.out.println("yess");
       Customer customer=customerRepo.findById(customerTokenRepo.findById(token).get().getPhoneNumber()).get();
       customer.setVaccinationStatus(customer.getVaccinationStatus()+1);
       customer.setMyVaccinationDate(java.time.LocalDate.now()+"");
       customer.setVaccinenameid(vaccineNameNo);
       customerRepo.save(customer);
    //in progress
        log.info("Registered for Dose "+doseNo);
        return "Registered for Dose "+doseNo;
    }
}
