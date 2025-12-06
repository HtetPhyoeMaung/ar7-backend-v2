package com.security.spring.thirdpartygames.markReport.service;

import com.security.spring.thirdpartygames.markReport.pojo.MarkReportRequest;
import com.security.spring.thirdpartygames.markReport.pojo.MarkReportResponse;

public interface MarkReportService {
    public MarkReportResponse marReportConfig(MarkReportRequest data);
}
