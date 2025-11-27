package com.beyondsales.beyondsales.security.controller;

import com.beyondsales.beyondsales.security.service.PasswordResetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    public static class PasswordResetRequest {
        @NotBlank
        @Email
        private String email;

        @NotBlank
        private String appUrl;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getAppUrl() { return appUrl; }
        public void setAppUrl(String appUrl) { this.appUrl = appUrl; }
    }

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@Valid @RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.requestPasswordReset(request.getEmail(), request.getAppUrl());
            return ResponseEntity.ok(Map.of(
                    "message", "Si l'email existe, un lien de réinitialisation a été envoyé."
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur lors de la demande de réinitialisation"));
        }
    }

    public static class PasswordResetConfirm {
        @NotBlank
        private String token;

        @NotBlank
        private String newPassword;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@Valid @RequestBody PasswordResetConfirm request) {
        boolean ok = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        if (ok) {
            return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Token invalide ou expiré."));
        }
    }
}
