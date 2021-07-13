package com.example.covidapp.Controllers.done;

import com.example.covidapp.Repo.done.*;
import com.example.covidapp.Services.done.CustomerService;
import com.example.covidapp.models.done.Customer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private VaccineNameRepo vaccineNameRepo;
    @Autowired
    private VaccinationCentreRepo vaccinationCentreRepo;
    @Autowired
    private VaccinationSlotRepo vaccinationSlotRepo;
    @Autowired
    private DateRepo dateRepo;
    @Autowired
    private CustomerTokenRepo customerTokenRepo;
    @Autowired
    private RegisteredCustomerRepo registeredCustomerRepo;

    @GetMapping("/login/customer")
    public String loginCustomer(@RequestBody(required = false) Customer customer) {
        log.info("[ customer :"+customer+" ]");
     if(customer==null){
         log.error("Phone no is not present :"+customer);
         return "Phone no is not present";
     }
        return customerService.loginCustomer(customer.getPhoneNumber());
    }

    @DeleteMapping("/logout/customer")
    public String logoutCustomer(@RequestHeader(value = "token", required = false) String token) {
        log.info("[ token:"+token+" called ]");
        if (token == null) {
            log.error("Token Not Present in headers :"+token);
            return "Token Not Present in headers";
        } else {
            return customerService.logoutCustomer(token);
        }
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@RequestBody(required = false) Customer customer) {
        log.info("[customer :"+customer+" called ]");
        try {
            if(customer==null){
                log.error("empty customer fields ,Customer { "+customer +" }");
                return "empty customer fields";
            }
            return customerService.registerCustomer(customer);
        } catch (Exception e) {
            log.error("Error occured : "+e);
            return e.getMessage();
        }
    }

    //    @GetMapping("/customer/isVaccinated")
//    public String isVaccinated(@RequestBody Customer customer) {
//        try {
//            return customerService.
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//    }
    @PostMapping("customer/registerForVaccine")
    public String registerForVaccine(@RequestParam(value = "vaccine_centre_id", required = false) String vaccine_centre_id,
                                     @RequestParam(value = "vaccine_name", required = false) String vaccine_name,
                                     @RequestParam(value = "date_no", required = false) String date_no,
                                     @RequestHeader(value = "token", required = false) String token) {

log.info("[ vaccine_centre_id:"+vaccine_centre_id+", vaccine_name:"+vaccine_name+", date_no:"+date_no+", token:"+token+" ]");
        if (token == null) {
            log.error("Token Not Present in headers");
            return "Token Not Present in headers"; }
        if (customerService.isCustomerAuthenticated(token) == -1) {
            log.error("Not Authenticated");
            return "Not Authenticated"; }
        if (vaccine_centre_id == null || vaccine_centre_id == "") {
            System.out.println(vaccine_centre_id+"111");
            log.error("Vaccine Centre Id not present");
            return "Vaccine Centre Id not present"; }
        if (vaccine_name == null || vaccine_name == "") {
            log.error("Vaccine Name not present");
            return "Vaccine Name not present"; }
        if (date_no == null || date_no == "") {
            log.error("Date No not present");
            return "Date No not present"; }


        if(isInteger(vaccine_centre_id)==false){
            log.error("Invalid vccination centre id : "+vaccine_centre_id);
            return "Invalid vccination centre id";
        }
        if(isInteger(date_no)==false){
            log.error("Invalid date no : "+date_no);
            return "Invalid date no";
        }


        // 1.vaccineCentreId
        int vaccineCentreId = Integer.parseInt(vaccine_centre_id);
        if (vaccinationCentreRepo.existsById(vaccineCentreId) == false) {
            log.error("Vaccination centre does not exhist with id : "+vaccine_centre_id);
            return "Vaccination centre does not exhist";
        }

        // 2.date
        int date = Integer.parseInt(date_no);
        if (dateRepo.existsById(date) == false) {
            log.error("Date No does not exhist with id : "+date_no);
            return "Date No does not exhist";
        }

        //3.vaccine Name no
        int vaccineNameNo;
        if (vaccineNameRepo.findByVaccineName(vaccine_name) == null) {
            log.error("Vaccine Name dosent exhist with name : "+vaccine_name);
            return "Vaccine Name dosent exhist";
        }
        vaccineNameNo = vaccineNameRepo.findByVaccineName(vaccine_name).getId();

        //4.ageNo
        int ageNo = customerRepo.findById(customerTokenRepo.findById(token).get().getPhoneNumber()).get().getAge();
        if (ageNo < 18) ageNo = 10;
        else if (ageNo < 45) ageNo = 20;
        else ageNo = 60;

        //5.doseNo
        int doseNo;
        int i = customerRepo.findById(customerTokenRepo.findById(token).get().getPhoneNumber()).get().getVaccinationStatus();
        if (i == 0) {
            doseNo = 1;
        }else if (i == 1) {
            log.error("already registered for dose 1");
            return "You have already registered for dose 1";
        } else if (i == 2) {
            doseNo = 2;
        }  else if (i == 3) {
            log.error("already registered for dose 2");
            return "you have already registered for dose 2";
        } else if(i>=4) {
            log.error("completely vaccinated");
            return "No more doses. You have been completely vaccinated";
        }else{
            doseNo=1;
        }

        //.............................................
        //if applying for dose 2 customer should be applying for same vaccine
        if(doseNo==2){
            if(customerRepo.findById(customerTokenRepo.findById(token).get().getPhoneNumber()).get().getVaccinenameid()
                    !=vaccineNameNo){
                log.error("can't apply for dose 2 of different vaccine");
                return "You can't apply for dose 2 of different vaccine";
            }
        }

        //....................................
        //already registered
        int status=customerRepo.findById(customerTokenRepo.findById(token).get().getPhoneNumber()).get().getVaccinationStatus();
        if(status==1){
            log.error("already registered for dose 1");
        return "You are already registered for dose 1";
        }
        if(status==3){
            log.error("already registered for dose 2");
            return "You are already registered for dose 2";
        }
//is vaccination slot for these details present
        if (vaccinationSlotRepo
                .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                        vaccineCentreId, vaccineNameNo, date, ageNo) == null) {
            log.error("vaccine center " + vaccineCentreId + " not available for these entries");
            return "vaccine center " + vaccineCentreId + " not available for these entries";
        }
        //.............................................
        //if vaccine has slot count

        if(doseNo==1){
            if(vaccinationSlotRepo
                    .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                            vaccineCentreId, vaccineNameNo, date, ageNo).getDoseNo1()<1){
                log.error("No vaccination slots available for dose 1 for these fields :" +
                        " vaccine Centre Id :"+vaccineCentreId+
                        "vaccineNameNo : "+vaccineNameNo+
                        " date : "+date+
                        " ageNo : "+ageNo);
                return "No vaccination slots available for dose 1 for these fields";
            }
        }
        if(doseNo==2){
            if(vaccinationSlotRepo
                    .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                            vaccineCentreId, vaccineNameNo, date, ageNo).getDoseNo2()<1){
                log.error("No vaccination slots available for dose 2 for these fields :" +
                        " vaccine Centre Id :"+vaccineCentreId+
                        "vaccineNameNo : "+vaccineNameNo+
                        " date : "+date+
                        " ageNo : "+ageNo);
                return "No vaccination slots available for dose 2 for these fields";
            }
        }


        //.............................................
        //customer can only register for second dose after 81 days
        long ph_no=customerTokenRepo.findById(token).get().getPhoneNumber();
        String date1=customerRepo.findById(ph_no).get().getMyVaccinationDate();
        String date2=dateRepo.findById(vaccinationSlotRepo
                .findVaccinationSlotsByVaccinecentreidAndVaccineNoAndDateNoAndAgeNo(
                        vaccineCentreId, vaccineNameNo, date, ageNo).getDateNo()).get().getMyDate();
        System.out.println(date1+"......///");
        if(date1.equals("")==false){
            String[]arr=date1.split("-");
            String[]arr2=date2.split("-");
            System.out.println(date1+"...........................");
            MyDateClass d1=new MyDateClass(Integer.parseInt(arr[2]+""),
                    Integer.parseInt(arr[1]),Integer.parseInt(arr[0]+""));
            MyDateClass d2=new MyDateClass(Integer.parseInt(arr2[2]+""),
                    Integer.parseInt(arr2[1]),Integer.parseInt(arr2[0]+""));
            if(getDifference(d1,d2)<81){
                log.error("can't book a slot before 81 days of vaccination date of 1st dose");
                return "You can't book a slot before 81 days of vaccination date of 1st dose.";
            }
        }


        //register the customer
        return customerService.registerForVaccine(token, vaccineCentreId, vaccineNameNo, date, ageNo, doseNo);
    }


    public boolean isInteger(String s) {
        try {
            int a = Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    //.....................................................................................
    //get difference between dates
    static class MyDateClass
    {
        int d, m, y;
        public MyDateClass(int d, int m, int y)
        {
            this.d = d;
            this.m = m;
            this.y = y;
        }
    }
    static int monthDays[] = {31, 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31};

    static int countLeapYears(MyDateClass d)
    {
        int years = d.y;
        if (d.m <= 2) { years--; }
        return years / 4 - years / 100 + years / 400;
    }
    static int getDifference(MyDateClass dt1, MyDateClass dt2)
    {
        int n1 = dt1.y * 365 + dt1.d;
        for (int i = 0; i < dt1.m - 1; i++)
        {
            n1 += monthDays[i];
        }
        n1 += countLeapYears(dt1);
        int n2 = dt2.y * 365 + dt2.d;
        for (int i = 0; i < dt2.m - 1; i++)
        {
            n2 += monthDays[i];
        }
        n2 += countLeapYears(dt2);
        return (n2 - n1);
    }
    //..................................................

}