package com.security.spring.utils;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupKey {
    private String ar7Id;
    private String gameCode;
    private String gameName;
    private String  gameTypeCode;

}
