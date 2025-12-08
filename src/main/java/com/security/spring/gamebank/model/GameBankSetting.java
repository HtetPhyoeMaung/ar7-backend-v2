package com.security.spring.gamebank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "game_bank_setting")
public class GameBankSetting {
    @Id
    private long id;

    private String agentCode;

    private String password;

    private String callBackUrl;
}
