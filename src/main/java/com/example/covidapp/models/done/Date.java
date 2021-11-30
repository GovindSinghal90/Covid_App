package com.example.covidapp.models.done;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Date {
    @Id
    private int id;
    private String myDate;
}
