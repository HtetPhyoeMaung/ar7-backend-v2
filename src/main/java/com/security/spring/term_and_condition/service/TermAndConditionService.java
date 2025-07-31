package com.security.spring.term_and_condition.service;

import com.security.spring.term_and_condition.dto.TermAndConditionRequest;
import com.security.spring.term_and_condition.dto.TermAndConditionResponse;
import com.security.spring.term_and_condition.dto.TermAndConditionType;

public interface TermAndConditionService {
    TermAndConditionResponse saveTermAndCondition(String token, TermAndConditionRequest termAndConditionRequest);

    TermAndConditionResponse updateTermAndCondition(String token, Long id, TermAndConditionRequest termAndConditionRequest);

    TermAndConditionResponse getTermAndConditionById(String token, Long id);

    TermAndConditionResponse deleteTermAndConditionById(String token, Long id);

    TermAndConditionResponse getAllTermAndCondition(String token);

    TermAndConditionResponse getByType(String token, TermAndConditionType termAndConditionType);
}
