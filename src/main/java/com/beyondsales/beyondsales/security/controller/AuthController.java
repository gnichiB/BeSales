package com.beyondsales.beyondsales.security.controller;

import com.beyondsales.beyondsales.security.jwt.JwtUtils;
import com.beyondsales.beyondsales.security.payload.request.LoginRequest;
import com.beyondsales.beyondsales.security.payload.request.SignupRequest;
import com.beyondsales.beyondsales.security.payload.response.ApiResponse;
import com.beyondsales.beyondsales.security.payload.response.JwtResponse;
import com.beyondsales.beyondsales.entity.Role;
import com.beyondsales.beyondsales.entity.User;
import com.beyondsales.beyondsales.repository.RoleRepository;
import com.beyondsales.beyondsales.repository.UserRepository;
import com.beyondsales.beyondsales.security.userdetails.CustomUserDetails;
import com.beyondsales.beyondsales.security.exception.RoleNotFoundException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationConfiguration authConfig,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) throws Exception {
        this.authenticationManager = authConfig.getAuthenticationManager();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(401)
                    .body(ApiResponse.failure("INVALID_CREDENTIALS", "Identifiants invalides"));
        } catch (DisabledException ex) {
            return ResponseEntity
                    .status(403)
                    .body(ApiResponse.failure("USER_DISABLED", "Compte désactivé"));
        } catch (LockedException ex) {
            return ResponseEntity
                    .status(423)
                    .body(ApiResponse.failure("USER_LOCKED", "Compte verrouillé"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(userDetails);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails); // optionnel

        List<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        JwtResponse payload = new JwtResponse(accessToken, refreshToken, userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.ok(ApiResponse.success(payload));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure("USERNAME_ALREADY_EXISTS", "Le nom d'utilisateur est déjà utilisé"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.failure("EMAIL_ALREADY_EXISTS", "L'email est déjà utilisé"));
        }

        // création utilisateur
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setCreatedAt(Instant.now());

        Set<String> strRoles = Optional.ofNullable(signUpRequest.getRoles()).orElse(Set.of("ROLE_USER"));
        Set<Role> roles = new HashSet<>();

        for (String r : strRoles) {
            Role role = roleRepository.findByName(r)
                    .orElseThrow(() -> new RoleNotFoundException("Role non trouvé: " + r));
            roles.add(role);
        }
        user.setRoles(roles);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(500)
                    .body(ApiResponse.failure("PERSISTENCE_ERROR", "Erreur lors de la sauvegarde de l'utilisateur"));
        }

        return ResponseEntity.ok(ApiResponse.successMessage("Utilisateur enregistré avec succès"));
    }
}
