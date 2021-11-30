package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.CustomerTokenClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerTokenRepo extends JpaRepository<CustomerTokenClass,String> {
     void deleteAllByPhoneNumber(long no);
    List<CustomerTokenClass> findAllByPhoneNumber(long no);
}
