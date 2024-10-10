package com.krzysiek.recruiting.repository;

import com.krzysiek.recruiting.model.RefreshToken;
import com.krzysiek.recruiting.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    int deleteByToken(String token);

    @Modifying
    @Transactional
    int deleteAllByUser(User user);
}
