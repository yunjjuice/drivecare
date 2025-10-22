package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("loginUser")
public class DealerDashboardController {
    @GetMapping("/dealer/dashboard")
    public String viewDashboard(@SessionAttribute("loginUser") UserInfoEntity loginUser, Model model) {
        return "dealer/dashboard";
    }
}
