package com.cts.healthconnect.ward.service;



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
}
