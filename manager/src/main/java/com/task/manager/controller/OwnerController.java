package com.task.manager.controller;

import com.task.manager.dto.LoginRequest;
import com.task.manager.model.Owner;
import com.task.manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/login")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public String showLoginPage() {
        return "login"; // Return the login view
    }

    @PostMapping("/login-form")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        Optional<Owner> owner = ownerService.login(loginRequest);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
            return "redirect:/tasks"; // Redirect to tasks if login is successful
        }
        model.addAttribute("error", "Invalid email or password");
        return "login"; // Return to login page if login fails
    }
}
