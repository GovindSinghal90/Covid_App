package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.Age;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgeRepo extends JpaRepository<Age,Integer> {
}
