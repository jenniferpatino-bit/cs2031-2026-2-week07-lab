package org.week07lab.auth.domain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.week07lab.auth.components.JwtService;
import org.week07lab.auth.dto.LoginRequest;
import org.week07lab.auth.dto.TokenResponse;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Delega la verificacion de credenciales al AuthenticationManager: email desconocido o
     * contrasena incorrecta lanzan BadCredentialsException (401).
     */
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())
        );
        return new TokenResponse(jwtService.generateToken((UserDetails) authentication.getPrincipal()));
    }
}
