package com.security.spring.withdraw.dto;

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
public class WithdrawResponse {
    private String message;
    private boolean status;
    private Integer statusCode;
    private List<WithdrawObj> withdrawObjList;
    private WithdrawObj withdrawObj;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
