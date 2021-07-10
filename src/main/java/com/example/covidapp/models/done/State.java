package com.example.covidapp.models.done;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class State {
    @Id
    private int id;
    private String stateName;
    private int stateCount;

    @ManyToOne
    @JoinColumn(name = "countryid",insertable = false,updatable = false)
    private Country country;

    private int countryid;

    @OneToMany(mappedBy = "state",cascade = CascadeType.ALL)
    List<City> cities;

    @JsonBackReference
    public Country getCountry() {
        return country;
    }

    @JsonManagedReference
    public List<City> getCities() {
        return cities;
    }
}
