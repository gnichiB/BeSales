package com.beyondsales.beyondsales.security.payload.response;

import java.util.List;

public record JwtResponse(
        String accessToken,
        String refreshToken,
        Long id,
        String username,
        List<String> roles
) {}
