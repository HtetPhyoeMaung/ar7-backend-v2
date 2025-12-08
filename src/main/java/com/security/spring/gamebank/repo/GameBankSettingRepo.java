package com.security.spring.gamebank.repo;

import com.security.spring.gamebank.model.GameBankSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameBankSettingRepo extends JpaRepository<GameBankSetting, Long> {
}
