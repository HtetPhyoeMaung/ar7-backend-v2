package com.security.spring.report.service;

import com.security.spring.report.dto.SpaceTechReportObj;
import com.security.spring.report.dto.SpaceTechGameReportResponse;

public interface ShanReportService {
    SpaceTechGameReportResponse saveSpaceTechGameReport(SpaceTechReportObj spaceTechReportObj);
}
