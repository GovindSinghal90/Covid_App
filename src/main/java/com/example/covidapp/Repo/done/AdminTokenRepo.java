package com.example.covidapp.Repo.done;

import com.example.covidapp.models.done.AdminTokenClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminTokenRepo extends JpaRepository<AdminTokenClass,String> {
    void deleteAllByPhoneNumber(long no);
    List<AdminTokenClass> findAllByPhoneNumber(long no);
}
