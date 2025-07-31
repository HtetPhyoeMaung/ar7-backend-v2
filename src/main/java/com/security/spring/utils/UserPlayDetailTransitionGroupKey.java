package com.security.spring.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPlayDetailTransitionGroupKey {
    private String ar7Id;
    private String  startDate;
    private String  endDate;
    private int gameTypeId;
}
