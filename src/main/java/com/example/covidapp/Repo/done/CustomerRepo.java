package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
