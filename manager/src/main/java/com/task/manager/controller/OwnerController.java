package com.task.manager.controller;

import com.task.manager.dto.LoginRequest;
import com.task.manager.jwt.JwtUtil;
import com.task.manager.model.Owner;
import com.task.manager.service.OwnerService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Controller
public class OwnerController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public OwnerController(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            Model model
    ){
        try{
            System.out.println("Attempting authentication for: " + email);
            System.out.println("Entered password: " + password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            System.out.println("Authentication successful for: " + email);
            if(userDetails == null){
                System.out.println("User not found");
                throw new UsernameNotFoundException("User not found");
            }

            String token = jwtUtil.generateToken(userDetails.getUsername());
            System.out.println("Generated token: " + token);

            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(1800);
            response.addCookie(jwtCookie);
            System.out.println("JWT token stored in cookie");

            return "redirect:/tasks";
        } catch(UsernameNotFoundException | BadCredentialsException e) {
            model.addAttribute("error", "Invalid username or password");
            System.out.println("Authentication failed: " + e.getMessage());
            return "redirect:/login";
        } catch(Exception e){
            model.addAttribute("error", "An unexpected error occurred");
            System.out.println("Authentication failed: " + e.getMessage());
            return "redirect:/login";
        }
    }
}