package com.example.covidapp.models.done;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class RegisteredCustomer {
    @Id
    private Long phNo;
    @ManyToOne
    @JoinColumn(name = "vaccinationSlotId",insertable = false,updatable = false)
    private VaccinationSlots vaccinationSlots;
    private int vaccinationSlotId;

    @JsonBackReference
    public VaccinationSlots getVaccinationSlots() {
        return vaccinationSlots;
    }
}
