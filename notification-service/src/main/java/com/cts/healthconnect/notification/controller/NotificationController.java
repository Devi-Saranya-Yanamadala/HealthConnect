package com.cts.healthconnect.notification.controller;

import com.cts.healthconnect.notification.dto.*;
import com.cts.healthconnect.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping
    public NotificationResponseDto send(@RequestBody NotificationRequestDto dto) {
        return service.sendNotification(dto);
    }

    @GetMapping
    public List<NotificationResponseDto> getAll(
            @RequestParam Long recipientId,
            @RequestParam String recipientType) {
        return service.getNotifications(recipientId, recipientType);
    }

    @PutMapping("/{id}/read")
    public void markRead(@PathVariable Long id) {
        service.markAsRead(id);
    }
}