package com.jubasbackend.service;

import com.jubasbackend.dto.BarberDTO;
import com.jubasbackend.dto.BarberOperationTimeDTO;
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

    public BarberOperationTimeDTO findById(UUID id) {
        return new BarberOperationTimeDTO(barberRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Barber Not Found")));
    }

    public BarberDTO save(Barber barber) {
        return new BarberDTO(barberRepository.save(barber));
    }

    public BarberOperationTimeDTO update(Barber barber) {
        Barber response = barberRepository.findById(barber.getId()).orElseThrow(NoSuchElementException::new);
        response.setOperationTime(barber.getOperationTime());
        return new BarberOperationTimeDTO(barberRepository.save(response));
    }

}