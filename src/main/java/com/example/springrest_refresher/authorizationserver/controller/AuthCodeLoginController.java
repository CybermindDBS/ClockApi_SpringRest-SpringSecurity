package com.example.springrest_refresher.authorizationserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthCodeLoginController {
    @GetMapping("/api-auth/login")
    public String showLoginPage(HttpServletRequest request, Model model, @RequestParam("error") Optional<String> error) {
        if (error.isPresent()) {
            Exception ex = (Exception) request
                    .getSession()
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            String msg;
            if (ex instanceof BadCredentialsException) {
                msg = "Username or password is incorrect.";
            } else if (ex instanceof SessionAuthenticationException) {
                msg = "You are already logged in elsewhere.";
            } else {
                msg = ex.getMessage();
            }
            model.addAttribute("errorMessage", msg);
        }

        model.addAttribute("heading", "Authorization Server | My Auth");
        model.addAttribute("formPostUrl", "/api-auth/login");
        model.addAttribute("showOauth2Login", false);

        return "login";
    }
}
