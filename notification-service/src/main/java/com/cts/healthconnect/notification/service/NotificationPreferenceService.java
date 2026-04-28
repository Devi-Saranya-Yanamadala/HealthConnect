
package com.cts.healthconnect.notification.service;

import com.cts.healthconnect.notification.dto.*;

public interface NotificationPreferenceService {
    NotificationPreferenceResponseDto savePreference(
            NotificationPreferenceRequestDto dto);
}