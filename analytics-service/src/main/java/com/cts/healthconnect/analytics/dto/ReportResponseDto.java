package com.cts.healthconnect.analytics.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private String reportId;
    private String reportName;
    private String status;
    private LocalDateTime createdAt;
    private String generatedBy;
}