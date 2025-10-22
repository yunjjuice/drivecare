package com.gitauto.drivecare.owner.controller;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.owner.dto.MainResponseDto;
import com.gitauto.drivecare.owner.dto.ReservationDetailRequestDto;
import com.gitauto.drivecare.owner.dto.ReservationRequestDto;
import com.gitauto.drivecare.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping("/dashboard")
    public String userMain(@SessionAttribute("loginUser") UserInfoEntity loginUser, Model model) {
        MainResponseDto mainResponseDto = ownerService.repairMain(loginUser.getUserId());
        model.addAttribute("main", mainResponseDto);

        return "owner/dashboard";
    }

    @GetMapping("/reservation")
    public String userReservation() {
        return "owner/reservation";
    }

    @PostMapping("/reservation/create")
    public String userReservation(@SessionAttribute("loginUser") UserInfoEntity loginUser, @ModelAttribute ReservationRequestDto reservationDto) {
        ownerService.reservation(loginUser.getUserId(), reservationDto);

        return "redirect:/owner/dashboard";
    }

    @GetMapping("/reservation/history")
    public String userReservationHistory(@SessionAttribute("loginUser") UserInfoEntity loginUser, Pageable pageable, Model model) {
        model.addAttribute("reservationList", ownerService.reservationHistory(loginUser.getUserId(), pageable));

        return "owner/reservation-history";
    }

    @PostMapping("/reservation/detail")
    public String userReservationDetail(@SessionAttribute("loginUser") UserInfoEntity loginUser,
                                        @ModelAttribute ReservationDetailRequestDto reservationDto, Model model) {
        model.addAttribute("reservationDetail", ownerService.reservationDetail(loginUser.getUserId(), reservationDto));

        return "owner/reservation-detail";
    }
}
