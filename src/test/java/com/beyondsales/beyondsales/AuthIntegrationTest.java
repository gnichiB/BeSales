package com.beyondsales.beyondsales;

import com.beyondsales.beyondsales.security.payload.request.LoginRequest;
import com.beyondsales.beyondsales.security.payload.request.SignupRequest;
import com.beyondsales.beyondsales.security.payload.request.TokenRefreshRequest;
import com.beyondsales.beyondsales.security.payload.response.JwtResponse;
import com.beyondsales.beyondsales.security.payload.response.TokenRefreshResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    public void testSignupSigninRefreshFlow() {
        // Signup
        SignupRequest signup = new SignupRequest("testuser", "testpass123", "test@example.com", null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SignupRequest> signupReq = new HttpEntity<>(signup, headers);
        ResponseEntity<String> signupResp = restTemplate.postForEntity(url("/api/auth/signup"), signupReq, String.class);
        System.out.println("Signup status = " + signupResp.getStatusCode());
        System.out.println("Signup body = " + signupResp.getBody());
        Assertions.assertTrue(signupResp.getStatusCode().is2xxSuccessful());

        // Signin (single POST)
        LoginRequest login = new LoginRequest("testuser", "testpass123");
        HttpEntity<LoginRequest> loginReq = new HttpEntity<>(login, headers);
        ResponseEntity<JwtResponse> loginResp = restTemplate.postForEntity(url("/api/auth/signin"), loginReq, JwtResponse.class);
        System.out.println("Signin status = " + loginResp.getStatusCode());
        System.out.println("Signin body = " + loginResp.getBody());
        Assertions.assertTrue(loginResp.getStatusCode().is2xxSuccessful());
        JwtResponse jwt = loginResp.getBody();
        Assertions.assertNotNull(jwt);
        Assertions.assertNotNull(jwt.accessToken());
        Assertions.assertNotNull(jwt.refreshToken());

        // Refresh (single POST, mapped)
        TokenRefreshRequest refreshReq = new TokenRefreshRequest(jwt.refreshToken());
        HttpEntity<TokenRefreshRequest> refreshEntity = new HttpEntity<>(refreshReq, headers);
        ResponseEntity<TokenRefreshResponse> refreshResp = restTemplate.postForEntity(url("/api/auth/refreshtoken"), refreshEntity, TokenRefreshResponse.class);
        System.out.println("Refresh status = " + refreshResp.getStatusCode());
        System.out.println("Refresh body = " + refreshResp.getBody());
        Assertions.assertTrue(refreshResp.getStatusCode().is2xxSuccessful());
        TokenRefreshResponse tokenRefreshResponse = refreshResp.getBody();
        Assertions.assertNotNull(tokenRefreshResponse);
        Assertions.assertNotNull(tokenRefreshResponse.getAccessToken());
        Assertions.assertNotNull(tokenRefreshResponse.getRefreshToken());
    }
}
