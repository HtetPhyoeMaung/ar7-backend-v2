package com.security.spring.report.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.commission.dto.CommissionConfirmObj;
import com.security.spring.commission.dto.CommissionObj;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsResponse {
    private String message;
    private boolean status;
    private int statusCode;
    private List<UserDetailObj> userDetailReportList;
    private List<CommissionConfirmReportObj> commissionConfirmReportObjList;
    private List<UserReportObj> userReportObjList;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
