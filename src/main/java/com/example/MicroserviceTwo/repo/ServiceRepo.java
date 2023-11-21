package com.example.MicroserviceTwo.repo;


import com.example.MicroserviceTwo.entity.ServiceTwoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepo extends JpaRepository<ServiceTwoEntity,Long> {


}
