package com.security.spring.gamesoft.markReport.service;

import com.security.spring.gamesoft.markReport.pojo.MarkReportRequest;
import com.security.spring.gamesoft.markReport.pojo.MarkReportRequestToProvider;
import com.security.spring.gamesoft.markReport.pojo.MarkReportResponse;
import com.security.spring.utils.ConstantInformationForGameSoft;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MarkReportServiceImpl implements MarkReportService{

    @Autowired
    private RestTemplate restTemplate;

    ConstantInformationForGameSoft constantObj = ConstantInformationForGameSoft.builder().build();
    String operatorCode = constantObj.getOperatorCode();
    String url = constantObj.getApiUrl();
    String thirdPartyRoute = url+"/Seamless/MarkReport";


    @Override
    public MarkReportResponse marReportConfig(MarkReportRequest data) {

        var requestObj = MarkReportRequestToProvider
                .builder()
                .operatorCode(operatorCode)
                .wagerIDs(data.getWagerIDs())
                .build();

//      Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        RequestEntity
        HttpEntity<MarkReportRequestToProvider> requestEntity = new HttpEntity<>(requestObj,headers);

//       Send Request With Post
        ResponseEntity<MarkReportResponse> response = restTemplate.exchange(thirdPartyRoute, HttpMethod.POST,requestEntity,
                new ParameterizedTypeReference<MarkReportResponse>(){});

        return response.getBody();
    }

}
