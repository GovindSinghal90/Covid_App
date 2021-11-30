package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.AdminRepo;
import com.example.covidapp.models.done.Admin;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private AdminTokenService tokenService;

    public String registerAdmin(Admin admin) {
        if(admin.getFirstName()==null||admin.getLastName()==null||admin.getFirstName()==""||admin.getLastName()=="") {
            log.error("Admin details not complete :"+admin);
            return "Incomplete Details";
        }
        if((admin.getPhoneNumber()+"").toString().length()!=10) {
            log.error("Phone Number not of length 10 digits :"+admin.getPhoneNumber());
            return "Phone Number Should be of 10 digits";
        }
        if(adminRepo.findById(admin.getPhoneNumber()).isPresent()) {
            log.error("Admin Exhists With this Phone Number :"+admin.getPhoneNumber());
            return "Admin Exhists With this Phone Number";
        }
        try{
            adminRepo.save(admin);
            return tokenService.addTokken(admin.getPhoneNumber());

        }
        catch (Exception e){
            log.error("Error occured :"+e);
            return e.getMessage();
        }

    }

    public String logoutAdmin(String token) {

        return tokenService.removeToken(token);
    }

    public String loginAdmin(long phoneNumber) {
        if((phoneNumber+"").toString().length()!=10) {
            log.error("Phone Number not of length 10 digits :"+phoneNumber);
            return "Phone Number Should be of 10 digits";
        }
        if(adminRepo.existsById(phoneNumber)){
            return tokenService.addTokken(phoneNumber);
        }
        else {
            log.error("No Account With this phone number :"+phoneNumber);
            return "No Account With this phone number... PLZZ LOGIN...";
        }
    }

    public Long isAdminAuthenticated(String token){

        return tokenService.isTokenPresent(token);
    }

}
