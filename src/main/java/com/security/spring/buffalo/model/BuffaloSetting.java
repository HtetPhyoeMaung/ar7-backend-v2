package com.security.spring.buffalo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BuffaloSetting {
    @Id
    private long id;

    private String agentCode;

    private String password;

    private String callBackUrl;
}
