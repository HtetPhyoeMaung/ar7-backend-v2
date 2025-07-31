package com.security.spring.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameTransactionGroupKey {
    private String ar7Id;
    private int gameTypeId;
}
