package com.security.spring.gamesoft.pullReportByWagerIDs.service;//package com.security.spring.gamesoft.pullReportByWagerIDs.service;
//
//import com.security.spring.gamesoft.pullReport.pojo.PullReportRequest;
//import com.security.spring.gamesoft.pullReport.pojo.PullReportResponse;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsRequest;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsResponse;
//import com.security.spring.gamesoft.wagger.entity.GameSoftWagger;
//import com.security.spring.utils.ConstantInformationForGameSoft;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class PullReportByWagerIDsServicesImpl implements PullReportByWagerIDsServices{
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    ConstantInformationForGameSoft constantObj = ConstantInformationForGameSoft.builder().build();
//    String operatorCode = constantObj.getOperatorCode();
//    private String apiUrl = constantObj.getApiUrl();
//    private String thirdPartyRoute = apiUrl+"/Seamless/PullReportByWagerIDs";
//
//
//    @Override
//    @Transactional
//    public PullReportByWagerIDsResponse wagers(PullReportByWagerIDsRequest data) {
//        System.out.println("PullReportByWagerIDsServices " + data);
//        System.out.println("operatorCode " + operatorCode);
//
////        RequestObject
//        var requestObj = PullReportByWagerIDsRequest
//                .builder()
//                .operatorCode(operatorCode)
//                .wagerIDs(data.getWagerIDs())
//                .build();
//
////      Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
////      Create Request Entity
//        HttpEntity<PullReportByWagerIDsRequest> requestEntity = new HttpEntity<>(requestObj,headers);
//
////       Send Request With Post
//        ResponseEntity<PullReportByWagerIDsResponse> response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST,requestEntity,
//                new ParameterizedTypeReference<PullReportByWagerIDsResponse>(){});
//
//        return response.getBody();
//    }
//}
