package com.jubasbackend.controller;

import com.jubasbackend.dto.ClientDTO;
import com.jubasbackend.entity.Client;
import com.jubasbackend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAll(){
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable UUID id){
        return ResponseEntity.ok(clientService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDTO> create(@RequestBody Client client){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(@RequestBody Client client){
        return ResponseEntity.ok(clientService.save(client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id){
        clientService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Client deleted");
    }
}