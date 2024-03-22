package com.jubasbackend.controller.request;

import java.util.UUID;

public record ProfileRequest(String name, String cpf, Boolean statusProfile, UUID userId) {
}
