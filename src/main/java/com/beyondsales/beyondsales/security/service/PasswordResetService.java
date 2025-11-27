package com.beyondsales.beyondsales.security.service;

import com.beyondsales.beyondsales.entity.PasswordResetToken;
import com.beyondsales.beyondsales.entity.User;
import com.beyondsales.beyondsales.repository.PasswordResetTokenRepository;
import com.beyondsales.beyondsales.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository,
                                EmailService emailService,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void requestPasswordReset(String email, String appUrl) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return; // do not reveal existence
        User user = userOpt.get();
        String token = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(1, ChronoUnit.HOURS);
        PasswordResetToken prt = new PasswordResetToken(token, expiry, user);
        tokenRepository.save(prt);

        String resetLink = appUrl + "/auth/reset?token=" + token;
        String text = "Bonjour,\n\nVous avez demandé la réinitialisation de votre mot de passe.\n"
                + "Utilisez ce lien (valide 1 heure) : " + resetLink + "\n\nSi vous n'êtes pas à l'origine de cette demande, ignorez ce message.";
        emailService.sendSimpleMessage(user.getEmail(), "Réinitialisation de mot de passe - BeyondSales", text);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> t = tokenRepository.findByToken(token);
        if (t.isEmpty()) return false;
        PasswordResetToken prt = t.get();
        if (prt.isUsed()) return false;
        if (prt.getExpiryDate().isBefore(Instant.now())) return false;
        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        prt.setUsed(true);
        tokenRepository.save(prt);
        return true;
    }
}
