package com.jubasbackend.dto;

import com.jubasbackend.entity.Client;

import java.util.UUID;

public record ClientDTO(UUID id, String name, Long cpf, boolean statusProfile) {

    public ClientDTO(Client client) {
        this(client.getId(), client.getName(), client.getCpf(), client.getStatusProfile());
    }
}
