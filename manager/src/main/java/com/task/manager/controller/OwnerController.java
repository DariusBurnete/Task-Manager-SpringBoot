package com.task.manager.controller;

import com.task.manager.dto.LoginRequest;
import com.task.manager.jwt.JwtUtil;
import com.task.manager.model.Owner;
import com.task.manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public String showLoginPage() {
        return "login"; // Return the login view
    }

    @PostMapping("/login-form")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Optional<Owner> owner = ownerService.login(loginRequest);
        if (owner.isPresent()) {
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}
