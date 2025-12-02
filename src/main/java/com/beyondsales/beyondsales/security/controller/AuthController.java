package com.beyondsales.beyondsales.security.controller;

import com.beyondsales.beyondsales.security.jwt.JwtUtils;
import com.beyondsales.beyondsales.security.payload.request.LoginRequest;
import com.beyondsales.beyondsales.security.payload.request.SignupRequest;
import com.beyondsales.beyondsales.security.payload.request.TokenRefreshRequest;
import com.beyondsales.beyondsales.security.payload.response.JwtResponse;
import com.beyondsales.beyondsales.security.payload.response.MessageResponse;
import com.beyondsales.beyondsales.security.payload.response.TokenRefreshResponse;
import com.beyondsales.beyondsales.entity.Role;
import com.beyondsales.beyondsales.entity.RefreshToken;
import com.beyondsales.beyondsales.entity.User;
import com.beyondsales.beyondsales.repository.RoleRepository;
import com.beyondsales.beyondsales.repository.UserRepository;
import com.beyondsales.beyondsales.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Récupérer l'utilisateur pour fournir l'id attendu par JwtResponse
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Erreur: utilisateur non trouvé."));

        // Créer et stocker un refresh token (la méthode renvoie le token brut à retourner au client)
        String refreshToken = refreshTokenService.createRefreshTokenForUser(user);

        return new JwtResponse(jwt, refreshToken, user.getId(), user.getUsername(), roles);
    }

    @PostMapping("/signup")
    public MessageResponse registerUser(@RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new MessageResponse("Erreur: Nom d'utilisateur déjà utilisé!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Erreur: Email déjà utilisé!");
        }

        // Création utilisateur
        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Erreur: Role non trouvé."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role foundRole = roleRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Erreur: Role non trouvé."));
                roles.add(foundRole);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponse("Utilisateur enregistré avec succès!");
    }

    @PostMapping("/refreshtoken")
    public TokenRefreshResponse refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        // vérification d'expiration (lance TokenRefreshException si expiré)
        refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();
        String newAccessToken = jwtUtils.generateJwtToken(user.getUsername());

        // Rotation: supprimer l'ancien refresh token et en créer un nouveau
        refreshTokenService.deleteByUserId(user.getId());
        String newRefreshToken = refreshTokenService.createRefreshTokenForUser(user);

        return new TokenRefreshResponse(newAccessToken, newRefreshToken);
    }
}
