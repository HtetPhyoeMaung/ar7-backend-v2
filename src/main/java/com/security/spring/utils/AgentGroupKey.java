package com.security.spring.utils;

import com.security.spring.commission.dto.AgentCommissionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Collector;

@Data
@AllArgsConstructor
public class AgentGroupKey {
    private String ar7Id;
    private String parentAgentId;


}
