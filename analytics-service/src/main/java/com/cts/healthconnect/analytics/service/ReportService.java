package com.cts.healthconnect.analytics.service;

import com.cts.healthconnect.analytics.dto.ReportRequestDto;
import com.cts.healthconnect.analytics.dto.ReportResponseDto;
import java.util.List;

public interface ReportService {
    // Generates metadata and stores snapshot
    ReportResponseDto generateReport(ReportRequestDto request);
    
    // Lists all reports for the dashboard
    List<ReportResponseDto> getAllReports();
    
    // Generates a PDF byte array for download
    byte[] generatePdfReport(String reportId);
}