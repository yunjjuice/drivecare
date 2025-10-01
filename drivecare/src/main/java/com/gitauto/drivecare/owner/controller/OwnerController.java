package com.gitauto.drivecare.owner.controller;

import com.gitauto.drivecare.owner.dto.ReservationRequestDto;
import com.gitauto.drivecare.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
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
//        ownerService.repairMain()

        return "owner/main";
    }

    @GetMapping("/reservation")
    public String userReservation(Model model) {
//        ownerService.repairMain()

        return "owner/reservation";
    }

    @PostMapping("/reservation/create")
    public String userReservation(@ModelAttribute ReservationRequestDto reservationDto) {
        ownerService.reservation(reservationDto);

        return "redirect://owner/main";
    }


    @GetMapping("/reservation/history")
    public String userReservationHistory(Model model) {
        model.addAttribute("reservation-list", ownerService.reservationHistory());

        return "owner/reservation-history";
    }

    @GetMapping("/reservation/detail")
    public String userReservationDetail(Model model) {
        model.addAttribute("reservation-detail", ownerService.reservationDetail());

        return "owner/reservation-detail";
    }
}
