package com.example.covidapp.models.done;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class Admin {
    @Id
    @JsonProperty("phone_number")
    private long phoneNumber;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name",required = true)
    private String lastName;
}
