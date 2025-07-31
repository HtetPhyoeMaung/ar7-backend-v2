package com.security.spring.bank.bankName.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankNameRequest {
    private Integer id;
    private Integer bankTypeId;
    private Integer bankNameId;
    private String bankName;
    private MultipartFile bankNameImage;
}
