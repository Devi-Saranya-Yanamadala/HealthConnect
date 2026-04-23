package com.cts.healthconnect.billing.repository;



import com.cts.healthconnect.billing.entity.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {
    Optional<InsuranceClaim> findByClaimNumber(String claimNumber);
}
