package com.cts.healthconnect.auth.repository;

import com.cts.healthconnect.auth.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByEmailAndOtp(String email, String otp);  // ← only this
    void deleteByEmail(String email);
}