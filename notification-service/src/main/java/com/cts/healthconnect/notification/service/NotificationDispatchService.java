package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.*;

public interface NotificationDispatchService {

    NotificationDispatchResponseDto dispatch(
            NotificationDispatchRequestDto dto);
}
