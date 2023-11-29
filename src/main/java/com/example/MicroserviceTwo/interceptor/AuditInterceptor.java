package com.example.MicroserviceTwo.interceptor;
import com.example.MicroserviceTwo.entity.ServiceTwoEntity;
import com.example.MicroserviceTwo.repo.ServiceRepo;
import com.example.MicroserviceTwo.service.ServiceTwoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

@Component
@Slf4j
public class AuditInterceptor implements HandlerInterceptor {
    private WebClient.Builder builder;
    @Autowired
    private ServiceRepo serviceRepo;
    @Autowired
    private ServiceTwoServiceImpl serviceTwoServiceImpl;

    //To add multithreading bean from config package
//    @Autowired
//    @Qualifier("asyncExecutor")
//    private ThreadPoolTaskExecutor asyncExecutor;
    Date requestTime = new Date(); // Capture the current date and time
    private long startTime;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Pre-handle logic to capture information
        startTime = System.currentTimeMillis();
        Date requestTime = new Date(); // Capture the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Request Time: " + dateFormat.format(requestTime));
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("Post Handler method");
    }

//    @Async("asyncExecutor")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        ServiceTwoEntity serviceTwoEntity=new ServiceTwoEntity();

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Date responseTime = new Date(); // Capture the current date and time for response
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get client ID from the header
        String clientId = request.getHeader("client-id");
        // Check if client ID is missing
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Client ID is required in the request header");
        }

        //for error trace
        String errorStackTrace = null;
        if (ex != null) {
            // Capture the exception stack trace in a variable
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            errorStackTrace = sw.toString();
            System.out.println(" error trace : " + errorStackTrace);
        }


        //for response
        ContentCachingResponseWrapper wrapper;
        if (response instanceof ContentCachingResponseWrapper) {
            wrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapper = new ContentCachingResponseWrapper(response);
        }
        String responseContent = getResponse(wrapper);


        //To get Query parameter
//        String name = request.getParameter("name");
        //ServiceOneEntity serviceOneEntity = new ServiceOneEntity();



        //for storing into database
        serviceTwoEntity.setClientId(clientId);
        serviceTwoEntity.setRequestTime(dateFormat.format(requestTime));
        serviceTwoEntity.setResponseTime(dateFormat.format(responseTime));
        serviceTwoEntity.setStatusCode(response.getStatus());
        serviceTwoEntity.setTimeTaken(String.valueOf(timeTaken));
        serviceTwoEntity.setRequestURI(request.getRequestURI());
        serviceTwoEntity.setRequestMethod(request.getMethod());
        serviceTwoEntity.setRequestHeaderName(getRequestHeaderNames(request));
        serviceTwoEntity.setContentType(request.getContentType());
        serviceTwoEntity.setRequestID(generateRequestId());
        serviceTwoEntity.setHostName(request.getServerName());
        serviceTwoEntity.setResponse(responseContent);
        serviceTwoEntity.setErrorTrace(errorStackTrace);
        serviceTwoEntity.setQueryParameter(request.getQueryString());
        serviceTwoEntity.setAuditTime(LocalDateTime.now());


        serviceRepo.save(serviceTwoEntity);

    //web client
      WebClient webClient = WebClient.create();
        webClient.post()
                .uri("http://localhost:8083/api/data")
                .body(BodyInserters.fromValue(serviceTwoEntity))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    private String getRequestHeaderNames(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headerNamesStr = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerNamesStr.append(headerName).append(", ");
        }
        return headerNamesStr.toString();
    }
    private String getResponse(ContentCachingResponseWrapper contentCachingResponseWrapper) {
        String response = IOUtils.toString(contentCachingResponseWrapper.getContentAsByteArray(), contentCachingResponseWrapper.getCharacterEncoding());
        return response;
    }

    //For Alpha-numeric Request Id
    public static String generateRequestId() {
        UUID uuid = UUID.randomUUID();
        String string = uuid.toString().replaceAll("-", ""); // Remove hyphens
        String alphanumericCharacters = string.replaceAll("[^A-Za-z0-9]", ""); // Remove non-alphanumeric characters
//        int randomIndex = (int) (Math.random() * alphanumericCharacters.length());
        while (alphanumericCharacters.length() < 10) {
            alphanumericCharacters += generateRandomAlphanumeric();
        }
        return alphanumericCharacters.substring(0, 10);
    }
    private static String generateRandomAlphanumeric() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int randomIndex = (int) (Math.random() * characters.length());
        return characters.substring(randomIndex, randomIndex + 1);
    }
}


