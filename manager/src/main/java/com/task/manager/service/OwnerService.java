package com.task.manager.service;

import com.task.manager.dto.LoginRequest;
import com.task.manager.model.Owner;
import com.task.manager.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean ownerExists(String email) {
        return ownerRepository.findByEmail(email).isPresent();
    }

    public void registerOwner(Owner owner) {
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        ownerRepository.save(owner);
    }

    public Optional<Owner> findByEmail(String email) {
        return ownerRepository.findByEmail(email);
    }

    public boolean verifyPassword(Owner owner, String rawPassword) {
        return passwordEncoder.matches(rawPassword, owner.getPassword());
    }

    public void deleteOwner(Owner owner) {
        ownerRepository.delete(owner);
    }
}
