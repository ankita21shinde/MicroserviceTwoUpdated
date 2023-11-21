package com.example.MicroserviceTwo.service;

import com.example.MicroserviceTwo.entity.ServiceTwoEntity;
import com.example.MicroserviceTwo.repo.ServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceTwoServiceImpl implements ServiceTwoService{

    @Autowired
    private ServiceRepo serviceRepo;
    @Override
    public ServiceTwoEntity save(ServiceTwoEntity serviceTwoEntity) {
        return serviceRepo.save(serviceTwoEntity);
    }
}
