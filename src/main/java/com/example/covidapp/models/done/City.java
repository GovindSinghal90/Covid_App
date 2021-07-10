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
public class City {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private int id;
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("city_count")
    private int cityCount;
    @JsonProperty("slots_count_minor")
    private int slotsCountMinor;
    @JsonProperty("slots_count_young")
    private int slotsCountYoung;
    @JsonProperty("slots_count_elder")
    private int slotsCountElder;

    @ManyToOne
    @JoinColumn(name = "stateid",insertable = false,updatable = false)
    private State state;

    @JsonProperty("state_id")
    private int stateid=-1;

    @OneToMany(mappedBy = "city",cascade = CascadeType.ALL)
    List<Pincode> pincodes;

    @JsonBackReference
    public State getState() {
        return state;
    }

    @JsonManagedReference
    public List<Pincode> getPincodes() {
        return pincodes;
    }


}
