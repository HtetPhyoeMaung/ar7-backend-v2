package com.security.spring.gamesoft.markReport.service;

import com.security.spring.gamesoft.markReport.pojo.MarkReportRequest;
import com.security.spring.gamesoft.markReport.pojo.MarkReportResponse;

public interface MarkReportService {
    public MarkReportResponse marReportConfig(MarkReportRequest data);
}
