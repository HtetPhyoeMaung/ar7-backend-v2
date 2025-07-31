package com.security.spring.gamesoft.getBetDetail.service;//package com.security.spring.gamesoft.getBetDetail.service;
//
//import com.security.spring.gamesoft.wagger.entity.GameSoftWagger;
//import com.security.spring.utils.ConstantInformationForGameSoft;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class GetBetDetailServiceImpl implements GetBetDetailService {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    ConstantInformationForGameSoft constantObj = ConstantInformationForGameSoft.builder().build();
//    String agentCode = constantObj.getOperatorCode();
//    String apiUrl = constantObj.getApiUrl();
//    String requestRoute = "/Report/BetDetail";
//
//    public GetBetDetailServiceImpl(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    @Override
//    public List<GameSoftWagger> getBetDetail(String waggerId) {
//        // Construct the URL with query parameters
//        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + requestRoute)
//                .queryParam("agentCode", agentCode)
//                .queryParam("WagerID", waggerId)
//                .toUriString();
//        System.out.println(agentCode);
//
//        // Create the request body as a Map
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("agentCode", agentCode);
//        requestBody.put("wagerId", waggerId);
//
//        // Create headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Create the HttpEntity with the request body and headers
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        try {
//            // Make the POST request
//            System.out.println(url);
//            GameSoftWagger[] response = restTemplate.postForObject(url, requestEntity, GameSoftWagger[].class);
//
//
//            // Convert the array to a list and return
//            return List.of(response);
//        } catch (HttpServerErrorException e) {
//            // Log the error details
//            System.err.println("Server error: " + e.getStatusCode());
//            System.err.println("Response body: " + e.getResponseBodyAsString());
//            // Handle the error appropriately
//            // For example, you can rethrow the exception or return an empty list
//            throw e;
//        } catch (Exception e) {
//            // Log the error details
//            System.err.println("Error: " + e.getMessage());
//            // Handle the error appropriately
//            // For example, you can rethrow the exception or return an empty list
//            throw e;
//        }
//    }
//}
