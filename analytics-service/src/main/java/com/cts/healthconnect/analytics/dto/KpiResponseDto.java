package com.cts.healthconnect.analytics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KpiResponseDto {
    private Long totalPatients;
    private Long totalAppointments;
    private Long totalAdmissions;
    private Long activeAdmissions;
    private Double totalRevenue;
}
