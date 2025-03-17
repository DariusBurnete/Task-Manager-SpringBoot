package com.task.manager.controller;

import com.task.manager.model.Owner;
import com.task.manager.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class DeleteAccController {
    @Autowired
    private OwnerService ownerService;

    @PostMapping("/deleteOwner")
    public String deleteOwner(@RequestParam String deleteEmail, @RequestParam String deletePassword, Model model, RedirectAttributes redirectAttributes) {
        Optional<Owner> ownerOptional = ownerService.findByEmail(deleteEmail);
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            // Verify password (assuming you have a method to check the password)
            if (ownerService.verifyPassword(owner, deletePassword)) {
                ownerService.deleteOwner(owner);
                redirectAttributes.addFlashAttribute("deleteError", "Account deleted successfully");
                return "redirect:/login"; // Redirect to a success page
            } else {
                model.addAttribute("deleteError", "Invalid password.");
            }
        } else {
            model.addAttribute("deleteError", "Owner not found.");
        }
        return "login";
    }
}
