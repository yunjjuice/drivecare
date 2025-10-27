package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.dto.ApiResponse;
import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.service.ReservationService;
import com.gitauto.drivecare.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationRestController {

    private final ReservationService reservationApiService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ReservationResponseDto>>> getReservationList(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtTokenProvider.getSubject(token);

        return ResponseEntity.ok(ApiResponse.ok(reservationApiService.reservationList(userId)));
    }
}
