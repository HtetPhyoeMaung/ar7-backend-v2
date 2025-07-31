package com.security.spring.term_and_condition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TermAndConditionDto {
    private Long id;
    private String context;
    private TermAndConditionType termAndConditionType;
}
