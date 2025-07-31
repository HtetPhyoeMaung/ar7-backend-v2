package com.security.spring.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExitInfosObj {
    private String  id;
    private Integer balance;
    private String description;
}
