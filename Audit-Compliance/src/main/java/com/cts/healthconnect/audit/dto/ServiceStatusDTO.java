package com.cts.healthconnect.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatusDTO {
    private String serviceName;
    private String status;      // e.g., UP, DOWN, STARTING
    private String port;
    private String instanceId;
}