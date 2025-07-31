package com.security.spring.report.repo;

import com.security.spring.report.entity.SpaceTechGameReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceTechGameReportRepo extends JpaRepository<SpaceTechGameReport, Long> {
    List<SpaceTechGameReport> findAllByCalculateStatus(boolean status);
}
