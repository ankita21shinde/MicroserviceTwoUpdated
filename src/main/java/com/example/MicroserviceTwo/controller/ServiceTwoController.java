package com.example.MicroserviceTwo.controller;


import com.example.MicroserviceTwo.entity.ServiceTwoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ServiceTwoController {
    @Autowired
    private WebClient webClient;
    public ServiceTwoController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }
    @PostMapping("/create-service2")
    public ResponseEntity<String> create(@RequestHeader("client-id") String clientId, @RequestParam(required = false)String name,@RequestParam int age) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Query parameter 'queryParameter' is required.");
        }
        ServiceTwoEntity serviceTwoEntity=new ServiceTwoEntity();
        serviceTwoEntity.setQueryParameter(name);
        return ResponseEntity.ok("User created successfully");
    }
    @GetMapping("/service-2")
    public String getPost(){
        return "Microservice 2 running successfully";
    }
}
