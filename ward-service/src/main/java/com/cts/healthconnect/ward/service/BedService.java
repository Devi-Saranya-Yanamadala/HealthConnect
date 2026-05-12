package com.cts.healthconnect.ward.service;



import com.cts.healthconnect.ward.dto.BedResponseDto;
import com.cts.healthconnect.ward.entity.WardType;
import java.util.List;

public interface BedService {

    List<String> getAvailableBeds(WardType wardType);
    List<BedResponseDto> getAllBeds(WardType wardType);
}
