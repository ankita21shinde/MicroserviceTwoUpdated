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
    public ResponseEntity<String> create(@RequestHeader("client-id") String clientId, @RequestParam(required = false)String name) {
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
//    public Mono<String> getMessage () {
//            // Simulate processing to retrieve a message (replace this with your actual logic)
//            String retrievedMessage = "Hello, this is the retrieved message!";
//
//            // Audit information
//            AuditData auditData = new AuditData("Microservice", "/get-message", "GET", null, retrievedMessage);
//
//            // Send audit data to central auditing service
//            return webClient.post()
//                    .uri("/api/audit")
//                    .bodyValue(auditData)
//                    .retrieve()
//                    .bodyToMono(String.class);
//        }



//    @PostMapping("/process-request")
//    public Mono<String> processRequest(@RequestBody RequestData requestData) {
//        // Process the request in Microservice 1
//        // ...

    // Audit information

//        AuditData auditData = new AuditData("Microservice 1", "/process-request", "POST", requestData, null);
//
//        // Send audit data to central auditing service
//        return webClient.post()
//                .uri("/api/audit")
//                .bodyValue(auditData)
//                .retrieve()
//                .bodyToMono(String.class);
//    }
    }
