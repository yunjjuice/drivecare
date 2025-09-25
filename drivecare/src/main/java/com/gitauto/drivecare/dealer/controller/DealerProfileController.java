package com.gitauto.drivecare.dealer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DealerProfileController {
    @GetMapping("/dealer/profile")
    public String viewCarCenterInfo() {
        return "dealer/profile";
    }
}
