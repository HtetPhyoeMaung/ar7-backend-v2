package com.security.spring.gamesoft.pullReport.service;//package com.security.spring.gamesoft.pullReport.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.security.spring.gamesoft.pullReport.pojo.PullReportRequest;
//import com.security.spring.gamesoft.pullReport.pojo.PullReportResponse;
//import com.security.spring.utils.ConstantInformationForGameSoft;
//import com.security.spring.utils.DateTimeGeneratorGameSoft;
//import com.security.spring.utils.SignGenerator;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//
//@Service
//@RequiredArgsConstructor
//public class PullReportServiceImpl implements PullReportService{
//
//    private final RestTemplate restTemplate;
//
//    ConstantInformationForGameSoft gameSoftConstObj = ConstantInformationForGameSoft.builder().build();
//    private String requestTime;
//    private String operatorCode = gameSoftConstObj.getOperatorCode();
//    private String methodName ="pullreport";
//    private String secretKey = gameSoftConstObj.getSecretKey();
//    private String sign;
//
//    private String apiUrl = gameSoftConstObj.getApiUrl();
//    private String thirdPartyRoute = apiUrl+"/Seamless/PullReport";
//
//    @Override
//    @Transactional
//    public PullReportResponse pullReportConfig(PullReportRequest data) {
//        requestTime = DateTimeGeneratorGameSoft.getCurrentDateTimeInUTC().toString();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // Parse the start date string into a LocalDateTime object
//        LocalDateTime startDateVal;
//        try {
//            startDateVal = LocalDateTime.parse(data.getStartDate(), formatter);
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Invalid date format for startDate. Expected format: yyyy-MM-dd HH:mm:ss", e);
//        }
//
//        LocalDateTime endDateVal = startDateVal.plusMinutes(5);
//        ZoneId myanmarZoneId = ZoneId.of("Asia/Yangon");
//        ZonedDateTime startZonedDateTime = startDateVal.atZone(myanmarZoneId);
//        ZonedDateTime endZonedDateTime = endDateVal.atZone(myanmarZoneId);
//        ZonedDateTime startUtcDateTime = startZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
//        ZonedDateTime endUtcDateTime = endZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
//
//        String startUtcDateTimeStr = startUtcDateTime.format(formatter);
//        String endUtcDateTimeStr = endUtcDateTime.format(formatter);
//
//        sign = SignGenerator.createSignature(operatorCode,requestTime,methodName,secretKey);
//
////      Request Object
//        var pullRequestObj = PullReportRequest
//                .builder()
//                .operatorCode(operatorCode)
//                .startDate(startUtcDateTimeStr)
//                .endDate(endUtcDateTimeStr)
//                .sign(sign)
//                .requestTime(requestTime)
//                .build();
////      Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
////      Create Request Entity
//        HttpEntity<PullReportRequest> requestEntity = new HttpEntity<>(pullRequestObj,headers);
//        System.out.println("Pull Back Request " + pullRequestObj);
//
////       Send Request With Post
//        ResponseEntity<PullReportResponse> response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST,requestEntity,
//                new ParameterizedTypeReference<PullReportResponse>(){});
//
//        try {
//            System.out.println(new ObjectMapper().writeValueAsString(response.getBody()));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return response.getBody();
//    }
//}
