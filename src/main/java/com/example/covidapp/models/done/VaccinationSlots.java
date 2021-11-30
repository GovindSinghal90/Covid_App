package com.example.covidapp.models.done;

import com.example.covidapp.models.done.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class VaccinationSlots {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "vaccinecentreid",insertable = false,updatable = false)
    private VaccinationCentre vaccinationCentre;
    @JsonProperty("vaccine_centre_id")
    private int vaccinecentreid;

    @ManyToOne
    @JoinColumn(name = "dateNo",insertable = false,updatable = false)
    private Date date;
    @JsonProperty("date_no")
    private int dateNo;

    @ManyToOne
    @JoinColumn(name = "vaccineNo",insertable = false,updatable = false)
    private VaccineName vaccineName;
    @JsonProperty("vaccine_no")
    private int vaccineNo;

    @ManyToOne
    @JoinColumn(name = "ageNo",insertable = false,updatable = false)
    private Age age;
    @JsonProperty("age_no")
    private int ageNo;
    private int doseNo1;
    private int doseNo2;


    @JsonBackReference
    public VaccinationCentre getVaccinationCentre() {
        return vaccinationCentre;
    }

    @OneToMany(mappedBy = "vaccinationSlots",cascade = CascadeType.ALL)
    List<RegisteredCustomer> registeredCustomerList;

    @JsonManagedReference
    public List<RegisteredCustomer> getRegisteredCustomerList() {
        return registeredCustomerList;
    }

    @Override
    public String toString() {
        return "VaccinationSlots{" +
                "id=" + id +
                ", vaccinecentreid=" + vaccinecentreid +
                ", dateNo=" + dateNo +
                ", vaccineNo=" + vaccineNo +
                ", ageNo=" + ageNo +
                ", doseNo1=" + doseNo1 +
                ", doseNo2=" + doseNo2 +
                '}';
    }
}
