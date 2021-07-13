package com.example.covidapp.models.done;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Country {
    @Id
    private int id;
    private String countryName;
    private int countryCount;

    @OneToMany(mappedBy = "country",cascade = CascadeType.ALL)
    List<State> states;

    @JsonManagedReference
    public List<State> getStates() {
        return states;
    }



}
