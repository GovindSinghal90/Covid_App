package com.example.covidapp.Controllers.done;

import com.example.covidapp.Repo.done.CustomerRepo;
import com.example.covidapp.Repo.done.RegisteredCustomerRepo;
import com.example.covidapp.Repo.done.VaccinationSlotRepo;
import com.example.covidapp.Services.done.AdminService;
import com.example.covidapp.Services.done.VaccinationCentreService;
import com.example.covidapp.models.done.Admin;
import com.example.covidapp.models.done.Customer;
import com.example.covidapp.models.done.VaccinationSlots;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RegisteredCustomerRepo registeredCustomerRepo;
    @Autowired
    private VaccinationSlotRepo vaccinationSlotRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private VaccinationCentreService vaccinationCentreService;

    @GetMapping("/login/admin")
    public String loginAdmin(@RequestBody(required = false) Admin admin)
    {
        log.info("[ admin :"+admin+" ]");
        if(admin==null){
            log.error("Phone no is not present :"+admin);
            return "Please pass phone no in admin fields";
        }
        return adminService.loginAdmin(admin.getPhoneNumber());
    }

    @DeleteMapping("/logout/admin")
    public String logoutAdmin(@RequestHeader(value = "token",required = false) String token){
        log.info("[ token:"+token+" ]");
            if (token == null) {
                log.error("Token not present in headers");
                return "Token Not Present in headers";
            }
            else {
            return adminService.logoutAdmin(token);
            }

    }

    @PostMapping("/register/admin")
    public String registerAdmin(@RequestBody(required = false) Admin admin,
                                @RequestHeader(value="key",required = false)String key) {
        log.info("[ admin :"+admin+"with key: "+key+" called ]");
        try {
            if(key==null){
                log.error("key is not present in headers");
                return "key is not present in headers";
            }
            if(key.equals("qwerty")) {
                if (admin == null){
                    log.error("Empty admin fields");
                    return "Empty admin fields";
                }
                return adminService.registerAdmin(admin);
            }
            else{
                log.error("Invalid key: "+key);
                return "Invalid key";
            }
        } catch (Exception e) {
            log.error("Error Occured : "+e);
            return e.getMessage();
        }
    }
    @PostMapping("/customer/setToVaccinated")
    public String setToVaccinated(@RequestParam(value = "phone_number", required = false) String phone_number,
                                  @RequestHeader(value = "token", required = false) String token){
        log.info("[ "+
                "phone_number: "+phone_number+
                " token "+token+" ]");
        if (token == null) {
            log.error("Token Not Present in headers");
        return "Token Not Present in headers"; }
        if (adminService.isAdminAuthenticated(token) == -1) {
            log.error("Not Authenticated");
            return "Not Authenticated";
        }
        if (phone_number== null || phone_number == "") {
            log.error("Phone Number not present");
            return "Phone Number not present";
        }
        System.out.println(phone_number);
        if(isInteger(phone_number)==false){
            log.error("Invalid phone_number : "+phone_number);
            return "Invalid phone_number";
        }
        System.out.println("yes");
        if(phone_number.length()!=10){
            log.error("Invalid phone_number,not of 10 digits length : "+phone_number);
            return "Invalid phone_number ,not of 10 digits length";
        }

        long phoneNumber=Long.parseLong(phone_number);
        if(registeredCustomerRepo.existsById(phoneNumber)==false){
            log.error("No vaccination registered for this phone number : "+phone_number);
            return "No vaccination registered for this phone number";
        }
        VaccinationSlots vaccinationSlots=vaccinationSlotRepo.findById(
                registeredCustomerRepo.findById(phoneNumber).get().getVaccinationSlotId()
        ).get();
        if(vaccinationSlots.getDateNo()!=1){
            log.error("You cannot vaccinate customer for other date slot");
            return "You cannot vaccinate customer for other date slot";
        }
        //marking as vaccinated
        Customer customer=customerRepo.findById(phoneNumber).get();
        customer.setVaccinationStatus(customer.getVaccinationStatus()+1);
        customer.setMyVaccinationDate(java.time.LocalDate.now()+"");
        System.out.println("yess");
        customerRepo.save(customer);
        System.out.println("yes");
        vaccinationCentreService.addVaccinatedCount(vaccinationSlots.getVaccinecentreid(),1);
        System.out.println("ye");
        registeredCustomerRepo.deleteById(phoneNumber);
        log.info("Vaccinated Successfully");
        return "Vaccinated Successfully";
    }


    public boolean isInteger(String s) {
        try {
            long a = Long.parseLong(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
