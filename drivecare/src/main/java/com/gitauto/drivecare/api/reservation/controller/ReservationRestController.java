package com.gitauto.drivecare.api.reservation.controller;

import com.gitauto.drivecare.api.reservation.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationRestController {

    private final ReservationService reservationApiService;

    @GetMapping("/list")
    public ResponseEntity<List<ReservationResponseDto>> getReservationList() {
        return ResponseEntity.ok().body(reservationApiService.reservationList());
    }

}
