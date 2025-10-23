package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.service.ReservationService;
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
        // TODO : 로그인 토큰 생기면 토큰 이용하여 해당 유저 데이터만 조회할 수 있도록 변경 필요
        return ResponseEntity.ok().body(reservationApiService.reservationList());
    }
}
