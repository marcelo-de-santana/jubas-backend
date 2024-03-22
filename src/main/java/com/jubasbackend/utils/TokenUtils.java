package com.jubasbackend.utils;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;


public class TokenUtils {

    public static String getAttribute(JwtAuthenticationToken jwt, String attribute) {
        return jwt.getTokenAttributes().get(attribute).toString();
    }

    public static boolean hasAuthority(JwtAuthenticationToken jwt, String authorityName){
        return jwt.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equals(authorityName));
    }
}
