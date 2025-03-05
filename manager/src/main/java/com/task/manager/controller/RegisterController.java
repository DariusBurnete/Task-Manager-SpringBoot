package com.task.manager.controller;

import com.task.manager.model.Owner;
import com.task.manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final OwnerService ownerService;

    public RegisterController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "register";
    }

    @PostMapping
    public String registerOwner(Owner owner, Model model) {
        if (ownerService.ownerExists(owner.getEmail())) {
            model.addAttribute("error", "Owner already exists");
            return "register";
        } else {
            ownerService.registerOwner(owner);
        }
        return "redirect:/login";
    }
}