package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.dealer.dto.DealerDashboardDto;
import com.gitauto.drivecare.dealer.service.DealerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class DealerDashboardController {

    private final DealerDashboardService dealerDashboardService;

    @GetMapping("/dealer/dashboard")
    public String viewDashboard(@SessionAttribute("loginUser") UserInfoEntity loginUser, Model model) {
        DealerDashboardDto dashboardData = dealerDashboardService.getTodayReservationsCount(loginUser);
        model.addAttribute("dashboard", dashboardData);
        return "dealer/dashboard";
    }
}
