package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.RegisteredCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredCustomerRepo extends JpaRepository<RegisteredCustomer,Long> {
}
