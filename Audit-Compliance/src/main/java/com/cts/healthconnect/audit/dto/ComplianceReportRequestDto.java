package com.cts.healthconnect.audit.dto;

import lombok.Data;

@Data
public class ComplianceReportRequestDto {
    private String reportName;
    private String generatedBy;
}