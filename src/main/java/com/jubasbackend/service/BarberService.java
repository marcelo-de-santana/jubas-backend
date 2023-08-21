package com.jubasbackend.service;

import com.jubasbackend.entity.Barber;
import com.jubasbackend.repository.BarberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarberService {
    @Autowired
    private BarberRepository barberRepository;

    public List<Barber> findALl(){
        return barberRepository.findAll();
    }

    public Barber save(Barber barber){
        return barberRepository.save(barber);
    }
}
