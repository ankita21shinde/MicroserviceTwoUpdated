package com.example.MicroserviceTwo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "serviceAuditing")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceTwoEntity {

    private String queryParameter;
    private LocalDateTime auditTime;
    private String clientId;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String serviceName="Microservice 2";
    //extra fields
    private String requestTime;
    private String responseTime;
    private int StatusCode;
    private String timeTaken;
    private String requestURI;
    private String requestMethod;
    private String requestHeaderName;
    private String contentType;
    private String requestID;
    private String hostName;
    private String response;
    private String errorTrace;
}
