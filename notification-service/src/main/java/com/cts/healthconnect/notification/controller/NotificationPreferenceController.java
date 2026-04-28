package com.cts.healthconnect.notification.controller;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.service.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService service;

    @PostMapping
    public NotificationPreferenceResponseDto savePreference(
            @RequestBody NotificationPreferenceRequestDto dto) {
        return service.savePreference(dto);
    }
}
