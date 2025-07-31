package com.security.spring.unit.dto;

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
public class TransitionResponse {
    private String message;
    private Integer statusCode;
    private Boolean status;
    private List<TransitionObj> transitionObjList;
    private List<AdminUnitCreateHistoryObj> adminUnitCreateHistoryObjList;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
}
