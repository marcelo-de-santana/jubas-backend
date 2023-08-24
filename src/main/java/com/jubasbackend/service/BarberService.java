package com.jubasbackend.service;

import com.jubasbackend.dto.BarberDTO;
import com.jubasbackend.entity.Barber;
import com.jubasbackend.repository.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BarberService {
    @Autowired
    private BarberRepository barberRepository;

    public List<BarberDTO> findAll() {
        return barberRepository.findAll().stream().map(BarberDTO::new).toList();
    }

    public BarberDTO findById(UUID id) {
        return new BarberDTO(barberRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Barber Not Found")));
    }

    public Barber save(Barber barber) {
        return barberRepository.save(barber);
    }
}
