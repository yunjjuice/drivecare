package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.dealer.dto.ReservationDto;
import com.gitauto.drivecare.dealer.service.DealerBookingService;
import com.gitauto.drivecare.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("loginUser")
@RequiredArgsConstructor
public class DealerBookingController {

    private final DealerBookingService service;

    @GetMapping("/dealer/booking")
    public String viewDashboard(@SessionAttribute("loginUser") UserEntity loginUser, Model model) {
        List<ReservationDto> reservationList = service.getBookings(loginUser.getCarCenterId());
        model.addAttribute("bookings", reservationList);
        return "dealer/booking";
    }
}
