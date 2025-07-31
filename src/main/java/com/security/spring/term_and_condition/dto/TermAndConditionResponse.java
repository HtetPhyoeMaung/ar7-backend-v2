package com.security.spring.term_and_condition.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.security.spring.term_and_condition.entity.TermAndCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TermAndConditionResponse {
    private boolean status;
    private String message;
    private TermAndConditionDto termAndCondition;
    private List<TermAndConditionDto> termAndConditionList;
}
