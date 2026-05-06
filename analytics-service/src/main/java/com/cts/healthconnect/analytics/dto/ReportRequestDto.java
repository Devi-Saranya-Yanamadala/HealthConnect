package com.cts.healthconnect.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReportRequestDto {
    @NotBlank(message = "Report name cannot be empty")
    private String reportName;

    @NotBlank(message = "Report type is required")
    @Pattern(regexp = "^(PDF|EXCEL|JSON)$", message = "Type must be PDF, EXCEL, or JSON")
    private String reportType;
}