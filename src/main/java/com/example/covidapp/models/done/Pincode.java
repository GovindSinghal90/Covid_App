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
public class Pincode {
    @Id
    @JsonProperty("pincode_id")
    private int pincode=-1;
    private int pincodeCount;
    private int slotsCountMinor;
    private int slotsCountYoung;
    private int slotsCountElder;

    @ManyToOne
    @JoinColumn(name = "cityid",insertable = false,updatable = false)
    private City city;
    @JsonProperty("city_id")
    private int cityid=-1;

    @OneToMany(mappedBy = "pincode",cascade = CascadeType.ALL)
    List<VaccinationCentre> vaccinationCentres;

    @JsonBackReference
    public City getCity() {
        return city;
    }

    @JsonManagedReference
    public List<VaccinationCentre> getVaccinationCentres() {
        return vaccinationCentres;
    }


}
