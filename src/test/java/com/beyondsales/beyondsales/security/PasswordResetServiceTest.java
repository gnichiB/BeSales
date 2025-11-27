package com.beyondsales.beyondsales.security;

import com.beyondsales.beyondsales.entity.PasswordResetToken;
import com.beyondsales.beyondsales.entity.User;
import com.beyondsales.beyondsales.repository.PasswordResetTokenRepository;
import com.beyondsales.beyondsales.repository.UserRepository;
import com.beyondsales.beyondsales.security.service.EmailService;
import com.beyondsales.beyondsales.security.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class PasswordResetServiceTest {

    @Test
    public void testResetFlow() {
        PasswordResetTokenRepository tokenRepo = Mockito.mock(PasswordResetTokenRepository.class);
        UserRepository userRepo = Mockito.mock(UserRepository.class);
        EmailService emailService = Mockito.mock(EmailService.class);
        PasswordEncoder encoder = Mockito.mock(PasswordEncoder.class);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("old");
        PasswordResetToken token = new PasswordResetToken("tok", Instant.now().plusSeconds(3600), user);

        Mockito.when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        Mockito.when(tokenRepo.save(any())).thenReturn(token);
        Mockito.when(tokenRepo.findByToken("tok")).thenReturn(Optional.of(token));
        Mockito.when(encoder.encode("newpass")).thenReturn("encodedNewPass");

        PasswordResetService svc = new PasswordResetService(tokenRepo, userRepo, emailService, encoder);
        svc.requestPasswordReset("test@example.com", "http://localhost:8080");
        boolean ok = svc.resetPassword("tok", "newpass");
        assertTrue(ok);
    }
}
