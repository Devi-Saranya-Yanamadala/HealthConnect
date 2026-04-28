package com.cts.healthconnect.notification.controller;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.service.NotificationDispatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationDispatchController {

    private final NotificationDispatchService dispatchService;

    @PostMapping("/dispatch")
    public NotificationDispatchResponseDto dispatch(
            @Valid @RequestBody NotificationDispatchRequestDto dto
    ) {
        return dispatchService.dispatch(dto);
    }
}
