package com.example.covidapp.models.done;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Age {
    @Id
    private int age;
    private String category;
}
