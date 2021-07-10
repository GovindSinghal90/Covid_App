package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.Pincode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PincodeRepo extends JpaRepository<Pincode,Integer> {
}
