package com.example.covidapp.Repo.done;
import com.example.covidapp.models.done.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepo extends JpaRepository<State,Integer> {
        State findByStateName(String name);
        }
