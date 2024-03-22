package com.jubasbackend.controller.request;

import jakarta.validation.constraints.Email;

public record AuthRequest(@Email String email, String password) {
}
