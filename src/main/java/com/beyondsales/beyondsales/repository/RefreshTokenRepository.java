package com.beyondsales.beyondsales.repository;

import com.beyondsales.beyondsales.entity.RefreshToken;
import com.beyondsales.beyondsales.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    int deleteByUser(User user);
}
