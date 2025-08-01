package com.security.spring.bank.bankAcc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccRequest {
    private Integer id;
    private Integer bankNameId;
    private String accountNum;
    private String accountName;
    private MultipartFile qrImg;
    private String description;
    private boolean accountStatus;
}
