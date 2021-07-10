package com.example.covidapp.models.done;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class VaccineName {
    @Id@GeneratedValue
    @JsonProperty("id")
    private int id;
    @JsonProperty("vaccine_name")
    private String vaccineName;
}
