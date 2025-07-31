package com.security.spring.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpaceTechReportObj {

    private String domain;
    private String game;
    private int matchNo;
    private int roomNO;
    private List<ExitInfosObj> exitInfos;
    private List<SpaceTechGameTransactionObj> transactions;
}
