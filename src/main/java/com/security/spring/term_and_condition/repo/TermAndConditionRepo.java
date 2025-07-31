package com.security.spring.term_and_condition.repo;

import com.security.spring.term_and_condition.dto.TermAndConditionType;
import com.security.spring.term_and_condition.entity.TermAndCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermAndConditionRepo extends JpaRepository<TermAndCondition, Long> {
    Optional<TermAndCondition> findByType(TermAndConditionType termAndConditionType);

    boolean existsByType(TermAndConditionType termAndConditionType);
}
