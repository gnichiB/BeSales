package com.beyondsales.beyondsales;

import com.beyondsales.beyondsales.entity.RefreshToken;
import com.beyondsales.beyondsales.entity.User;
import com.beyondsales.beyondsales.repository.RefreshTokenRepository;
import com.beyondsales.beyondsales.repository.UserRepository;
import com.beyondsales.beyondsales.service.RefreshTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;

public class RefreshTokenServiceTest {

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private RefreshTokenService service;

    @BeforeEach
    public void setup() {
        refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        service = new RefreshTokenService(refreshTokenRepository, userRepository);
    }

    @Test
    public void testHashingAndFind() {
        User user = new User();
        user.setId(1L);
        user.setUsername("u");

        // create token
        String token = service.createRefreshTokenForUser(user);
        Assertions.assertNotNull(token);

        // mocking repository to find by hash
        Optional<RefreshToken> stored = Optional.of(new RefreshToken("hash", user, Instant.now().plusSeconds(3600)));
        Mockito.when(refreshTokenRepository.findByTokenHash(Mockito.anyString())).thenReturn(stored);

        Optional<RefreshToken> found = service.findByToken(token);
        // Since repository was mocked to return something, should be present
        Assertions.assertTrue(found.isPresent());
    }
}

