package com.beyondsales.beyondsales.security.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class SignupRequest {
    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Size(min = 6, max = 128)
    private String password;

    @NotBlank @Email
    private String email;

    private Set<String> roles;

    // getters / setters
    // constructeur no-arg requis par Jackson
}
