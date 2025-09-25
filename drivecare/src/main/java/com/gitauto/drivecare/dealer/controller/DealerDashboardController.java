package com.gitauto.drivecare.dealer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DealerDashboardController {
    @GetMapping("/dealer/dashboard")
    public String viewDashboard() {
        return "dealer/dashboard";
    }
}
