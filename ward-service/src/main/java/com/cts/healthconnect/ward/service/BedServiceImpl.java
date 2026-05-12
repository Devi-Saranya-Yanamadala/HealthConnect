package com.cts.healthconnect.ward.service;



import com.cts.healthconnect.ward.dto.BedResponseDto;
import com.cts.healthconnect.ward.entity.Bed;
import com.cts.healthconnect.ward.entity.WardType;
import com.cts.healthconnect.ward.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedRepository repository;

    @Override
    public List<String> getAvailableBeds(WardType wardType) {

        return repository.findByWardTypeAndOccupiedFalse(wardType)
                .stream()
                .map(Bed::getBedNumber)
                .toList();
    }
    @Override
    public List<BedResponseDto> getAllBeds(WardType wardType) {
        return repository.findByWardType(wardType)
            .stream()
            .map(bed -> BedResponseDto.builder()
                .bedNumber(bed.getBedNumber())
                .wardType(bed.getWardType().name())
                .status(Boolean.TRUE.equals(bed.getOccupied()) ? "OCCUPIED" : "AVAILABLE")
                .build())
            .toList();
    }
}
