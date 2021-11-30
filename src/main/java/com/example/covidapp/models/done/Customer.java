package com.example.covidapp.models.done;

import com.example.covidapp.models.done.VaccineName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString
public class Customer {
    @Id
    @JsonProperty("phone_number")
    private long phoneNumber;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name",required = true)
    private String lastName;
    @JsonProperty(value = "age")
    private int age;
    private int vaccinationStatus;
    private String myVaccinationDate;

    private int vaccinenameid=0;
}
