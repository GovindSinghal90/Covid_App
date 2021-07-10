package com.example.covidapp.models.done;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class VaccinationCentre {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private int id;
    @JsonProperty("vaccination_centre_address")
    private String vaccinationCentreAddress="";
    private int vaccinationCentreCount;
    private int slotsCountMinor;
    private int slotsCountYoung;
    private int slotsCountElder;

    @ManyToOne
    @JoinColumn(name = "pincodeid",insertable = false,updatable = false)
    private Pincode pincode;

    @JsonProperty("pincode_id")
    private int pincodeid=-1;

    @JsonBackReference
    public Pincode getPincode() {
        return pincode;
    }

    @OneToMany(mappedBy = "vaccinationCentre",cascade = CascadeType.ALL)
    private List<VaccinationSlots> vaccinationSlots;

    @JsonManagedReference
    public List<VaccinationSlots> getVaccinationSlots() {
        return vaccinationSlots;
    }

    @Override
    public String toString() {
        return "VaccinationCentre{" +
                "id=" + id +
                ", vaccinationCentreAddress='" + vaccinationCentreAddress + '\'' +
                ", vaccinationCentreCount=" + vaccinationCentreCount +
                ", slotsCountMinor=" + slotsCountMinor +
                ", slotsCountYoung=" + slotsCountYoung +
                ", slotsCountElder=" + slotsCountElder +
                ", pincode=" + pincode +
                ", pincodeid=" + pincodeid +
                ", vaccinationSlots=" + vaccinationSlots +
                '}';
    }
}
