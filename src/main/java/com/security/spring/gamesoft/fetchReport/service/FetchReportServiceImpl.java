package com.security.spring.gamesoft.fetchReport.service;//package com.security.spring.gamesoft.fetchReport.service;
//
//import com.security.spring.gamesoft.fetchReport.pojo.FetchReportRequest;
//import com.security.spring.gamesoft.fetchReport.pojo.FetchReportResponse;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsRequest;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsResponse;
//import com.security.spring.utils.ConstantInformationForGameSoft;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@RequiredArgsConstructor
//public class FetchReportServiceImpl implements FetchReportService{
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    ConstantInformationForGameSoft constantObj = ConstantInformationForGameSoft.builder().build();
//    String operatorCode = constantObj.getOperatorCode();
//    private String apiUrl = constantObj.getApiUrl();
//    private String thirdPartyRoute = apiUrl+"/Seamless/FetchReport";
//
//    @Override
//    @Transactional
//    public FetchReportResponse fetchReportServiceConfig() {
//
////        RequestObject
//        var requestObj = FetchReportRequest
//                .builder()
//                .operatorCode(operatorCode)
//                .build();
//
////      Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
////      Create Request Entity
//        HttpEntity<FetchReportRequest> requestEntity = new HttpEntity<>(requestObj,headers);
//
////       Send Request With Post
//        ResponseEntity<FetchReportResponse> response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST,requestEntity,
//                new ParameterizedTypeReference<FetchReportResponse>(){});
//
//        return response.getBody();
//    }
//}
