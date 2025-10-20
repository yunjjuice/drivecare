package com.gitauto.drivecare.owner.controller;

import com.gitauto.drivecare.owner.dto.MainResponseDto;
import com.gitauto.drivecare.owner.dto.ReservationDetailRequestDto;
import com.gitauto.drivecare.owner.dto.ReservationRequestDto;
import com.gitauto.drivecare.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping
    public String userMain(Model model) {
        MainResponseDto mainResponseDto = ownerService.repairMain();
        model.addAttribute("main", mainResponseDto);

        return "owner/main";
    }

    @GetMapping("/reservation")
    public String userReservation() {
        return "owner/reservation";
    }

    @PostMapping("/reservation/create")
    public String userReservation(@ModelAttribute ReservationRequestDto reservationDto) {
        ownerService.reservation(reservationDto);

        return "redirect:/owner";
    }

    @GetMapping("/reservation/history")
    public String userReservationHistory(Pageable pageable, Model model) {
        model.addAttribute("reservationList", ownerService.reservationHistory(pageable));

        return "owner/reservation-history";
    }

    @PostMapping("/reservation/detail")
    public String userReservationDetail(@ModelAttribute ReservationDetailRequestDto reservationDto, Model model) {
        model.addAttribute("reservationDetail", ownerService.reservationDetail(reservationDto));

        return "owner/reservation-detail";
    }
}
