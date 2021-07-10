package com.example.covidapp.models.done;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class CustomerTokenClass {
    @Id
    private String token;
    private long phoneNumber;
}
