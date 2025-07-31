package com.security.spring.gamesoft.pullPendingRecords.service;//package com.security.spring.gamesoft.pullPendingRecords.service;
//
//import com.security.spring.gamesoft.pullPendingRecords.pojo.PullPendingRecordsProviderRequest;
//import com.security.spring.gamesoft.pullPendingRecords.pojo.PullPendingRecordsRequest;
//import com.security.spring.gamesoft.pullPendingRecords.pojo.PullPendingRecordsResponse;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsRequest;
//import com.security.spring.gamesoft.pullReportByWagerIDs.pojo.PullReportByWagerIDsResponse;
//import com.security.spring.utils.ConstantInformationForGameSoft;
//import com.security.spring.utils.DateTimeGeneratorGameSoft;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.time.ZonedDateTime;
//
//@Service
//@RequiredArgsConstructor
//public class PullPendingRecordsServiceImpl implements PullPendingRecordsService{
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    ConstantInformationForGameSoft constantObj = ConstantInformationForGameSoft.builder().build();
//    String operatorCode = constantObj.getOperatorCode();
//    String url = constantObj.getApiUrl();
//    String thirdPartyRoute = url+"/Seamless/PullPendingRecords";
//
//    @Override
//    @Transactional
//    public PullPendingRecordsResponse pullPendingRecordConfig(PullPendingRecordsRequest data) {
//        System.out.println(data);
//        String startTimeWithUTC = String.valueOf(DateTimeGeneratorGameSoft.convertStringToUTC(data.getStartDate()));
//        String endTimeWithUTC = String.valueOf(DateTimeGeneratorGameSoft.addHours(DateTimeGeneratorGameSoft.convertStringToUTC(data.getStartDate()),23));
//        var requestObj = PullPendingRecordsProviderRequest
//                .builder()
//                .operatorCode(operatorCode)
//                .productID(data.getProductID())
//                .gameType(data.getGameType())
//                .startDate(startTimeWithUTC)
//                .endDate(endTimeWithUTC)
//                .build();
//
////      Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
////      Create Request Entity
//        HttpEntity<PullPendingRecordsProviderRequest> requestEntity = new HttpEntity<>(requestObj,headers);
//
////       Send Request With Post
//        ResponseEntity<PullPendingRecordsResponse> response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST,requestEntity,
//                new ParameterizedTypeReference<PullPendingRecordsResponse>(){});
//
//        return response.getBody();
//    }
//}
