package com.ffa.tournaments.shared.infrastructure.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/verify-email",
            "/confirm-email-change",
            "/forgot-password",
            "/reset-password",
            "/auth",
            "/onboarding",
            "/dashboard",
            "/profile",
            "/teams",
            "/teams/**",
            "/tournaments",
            "/tournaments/**",
            "/support/login",
            "/support",
            "/support/**"
    })
    public String forwardToFrontend() {
        return "forward:/index.html";
    }
}