package com.jubasbackend.service;

import com.jubasbackend.config.SecurityConfig;
import com.jubasbackend.controller.request.AuthRequest;
import com.jubasbackend.controller.response.TokenResponse;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;

@AllArgsConstructor
@Service
public class TokenService {

    final SecurityConfig securityConfig;
    final JwtEncoder jwtEncoder;
    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;

    public TokenResponse generate(AuthRequest request) {
        var user = getByEmail(request.email());

        if (!user.isCorrectPassword(request, passwordEncoder))
            throw new BadCredentialsException("Incorrect Email or Password.");

        var currentTime = Instant.now().atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
        var expiresIn = securityConfig.getDuration();
        var permission = user.getPermission().toString();

        var claims = JwtClaimsSet.builder()
                .issuer(securityConfig.getIssuer())
                .subject(user.getId().toString())
                .claim("scope", permission)
                .issuedAt(currentTime)
                .expiresAt(currentTime.plusSeconds(expiresIn))
                .build();


        var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new TokenResponse(token);

    }

    User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new BadCredentialsException("E-mail or password is invalid!"));
    }
}
