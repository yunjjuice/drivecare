package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.database.car_center.entity.CarCenterEntity;
import com.gitauto.drivecare.dealer.service.DealerProfileService;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("loginUser")
@RequiredArgsConstructor
public class DealerProfileController {

    private final DealerProfileService service;

    @GetMapping("/dealer/profile")
    public String viewCarCenterInfo(@SessionAttribute("loginUser") UserInfoEntity loginUser, Model model) {
        // 정비소 정보 가져오기
        CarCenterEntity carCenter = service.getCarCenterInfoByUserId(loginUser.getUserId());
        model.addAttribute("carCenter", carCenter);
        return "dealer/profile";
    }
}
