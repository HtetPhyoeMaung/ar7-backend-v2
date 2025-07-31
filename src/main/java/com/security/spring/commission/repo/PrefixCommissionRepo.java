package com.security.spring.commission.repo;

import com.security.spring.commission.entity.PrefixCommission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrefixCommissionRepo extends JpaRepository<PrefixCommission,Integer> {
    PrefixCommission findByAgentAr7IdAndAgentWinLoseAmountGreaterThan(String agentAr7Id, int i);

    PrefixCommission findByAgentAr7IdAndMasterAr7IdAndSeMasterAr7Id(String agentId, String masterId, String seMasterId);

    PrefixCommission findByAgentAr7Id(String agentAr7Id);
}
