package com.security.spring.deposit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class DepositResponse {
    private String message;
    private Boolean status;
    private Integer statusCode;
    private List<DepositObj> depositObjList;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
