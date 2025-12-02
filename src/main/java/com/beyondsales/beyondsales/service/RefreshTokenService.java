package com.beyondsales.beyondsales.service;

import com.beyondsales.beyondsales.entity.RefreshToken;
import com.beyondsales.beyondsales.entity.User;
import com.beyondsales.beyondsales.repository.RefreshTokenRepository;
import com.beyondsales.beyondsales.repository.UserRepository;
import com.beyondsales.beyondsales.security.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration:2592000000}") // default 30 days in ms
    private Long refreshTokenDurationMs = 2592000000L;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String createRefreshTokenForUser(User user) {
        // Supprimer tout refresh token existant pour cet utilisateur (évite violation d'unicité)
        try {
            refreshTokenRepository.deleteByUser(user);
        } catch (Exception ignored) {
            // ignore, deleteByUser may return 0 if none
        }

        // génère un token brut (UUID), stocke seulement son hash
        String token = UUID.randomUUID().toString();
        String tokenHash = sha256(token);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setTokenHash(tokenHash);
        refreshToken = refreshTokenRepository.save(refreshToken);

        return token; // retourne le token brut au client
    }

    public Optional<RefreshToken> findByToken(String token) {
        String tokenHash = sha256(token);
        return refreshTokenRepository.findByTokenHash(tokenHash);
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getTokenHash(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return refreshTokenRepository.deleteByUser(user);
    }
}
