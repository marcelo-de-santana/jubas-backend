package com.jubasbackend.controller.response;

public record TokenResponse(String accessToken, Long expiresIn, UserResponse user) {
}
