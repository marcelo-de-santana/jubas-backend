package com.jubasbackend.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import lombok.Getter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Getter
    @Value("${jwt.issuer}")
    private String issuer;

    @Getter
    @Value("${jwt.duration}")
    private Long duration;

    @Value("${jwt.keys.public}")
    private String publicKeyStr;

    @Value("${jwt.keys.private}")
    private String privateKeyStr;

    RSAPublicKey publicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var decoded = Base64.decodeBase64(publicKeyStr);
        var keySpec = new X509EncodedKeySpec(decoded);
        var keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    RSAPrivateKey privateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        var decoded = Base64.decodeBase64(privateKeyStr);
        var keySpec = new PKCS8EncodedKeySpec(decoded);
        var keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() throws InvalidKeySpecException, NoSuchAlgorithmException {
        return NimbusJwtDecoder.withPublicKey(publicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws InvalidKeySpecException, NoSuchAlgorithmException {
        var jwk = new RSAKey.Builder(publicKey()).privateKey(privateKey())
                .build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(CustomJWT.converter())))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api-docs/**", "/swagger-ui/**")
//                        .permitAll()
//
//                        .requestMatchers(GET,
//                                "/appointments",
//                                "/appointments/days-of-attendance",
//                                "/categories")
//                        .permitAll()
//
//                        .requestMatchers(POST,
//                                "/auth",
//                                "/users")
//                        .permitAll()
//
//                        .requestMatchers(GET, "/users")
//                        .hasRole(PermissionType.ADMIN.toString())

                                .anyRequest()
                        .permitAll()
                ).build();
    }
}
