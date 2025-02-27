package com.task.manager.service;

import com.task.manager.dto.LoginRequest;
import com.task.manager.model.Owner;
import com.task.manager.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;

    public Optional<Owner> login(LoginRequest loginRequest) {
        Optional<Owner> owner = ownerRepository.findByEmail(loginRequest.getEmail());
        if (owner.isPresent() && owner.get().getPassword().equals(loginRequest.getPassword())) {
            return owner;
        }
        return Optional.empty();
    }
}
