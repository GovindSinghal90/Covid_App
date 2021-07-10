package com.example.covidapp.Services.done;

import com.example.covidapp.Repo.done.CustomerTokenRepo;
import com.example.covidapp.models.done.CustomerTokenClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
@Log4j2
public class CustomerTokenService {

    @Autowired
    private CustomerTokenRepo tokenRepo;

    @Transactional
    public String addTokken(long no){
        CustomerTokenClass token=new CustomerTokenClass();
        String key=createToken();
        while(tokenRepo.existsById(key)==true){
            key=createToken();
        }

        token.setToken(createToken());
        token.setPhoneNumber(no);
        try{
            if((tokenRepo.findAllByPhoneNumber(no)).size()>=1) {
                tokenRepo.deleteAllByPhoneNumber(no);
            }
            tokenRepo.save(token);
            return token.getToken();
        }
        catch (Exception e){
            log.error("Error Occured :"+e);
            return  e.getMessage();
        }

    }

    public String removeToken(String token){
        if(tokenRepo.existsById(token)){
            tokenRepo.deleteById(token);
            return "Logout Successful";
        }
        else{
            log.error("Invalid Token : "+token);
            return "Invalid Token";
        }
    }

    private String createToken(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
    public long isTokenPresent(String token){
        if(tokenRepo.existsById(token)){
            return tokenRepo.findById(token).get().getPhoneNumber();
        }
        log.error("Token is not present : "+token);
        return -1;
    }
}
