package com.task.manager.controller;

import com.task.manager.model.Owner;
import com.task.manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//Used to display the current owner and id on the pages
@ControllerAdvice
public class BaseController {
    @Autowired
    private OwnerService ownerService;

    @ModelAttribute
    public void addOwnerDetails(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            ownerService.findByEmail(email).ifPresent(owner -> model.addAttribute("owner", owner));
        }
    }
}
