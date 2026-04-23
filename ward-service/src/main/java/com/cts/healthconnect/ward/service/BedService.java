package com.cts.healthconnect.ward.service;



import com.cts.healthconnect.ward.entity.WardType;
import java.util.List;

public interface BedService {

    List<String> getAvailableBeds(WardType wardType);
}
