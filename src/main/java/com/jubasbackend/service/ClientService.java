package com.jubasbackend.service;

import com.jubasbackend.entity.Client;
import com.jubasbackend.repository.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Client save(Client client){
        return clientRepository.save(client);
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public Client findById(UUID id){
        return clientRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Client not found."));
    }

    public void deleteById(UUID id){
        clientRepository.deleteById(id);
    }
}
