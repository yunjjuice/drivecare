package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.dto.ApiResponse;
import com.gitauto.drivecare.api.dto.ReservationResponseDto;
import com.gitauto.drivecare.api.dto.VehicleHealthDetailResponseDto;
import com.gitauto.drivecare.api.dto.VehicleHealthSaveRequestDto;
import com.gitauto.drivecare.api.service.VehicleHealthService;
import com.gitauto.drivecare.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-health")
@RequiredArgsConstructor
public class VehicleHealthRestController {

    private final VehicleHealthService vehicleHealthService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<VehicleHealthDetailResponseDto>> getVehicleHealthDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtTokenProvider.getSubject(token);

        return ResponseEntity.ok(ApiResponse.ok(vehicleHealthService.vehicleHealthDetail(userId)));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Void>> saveVehicleHealth(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                               @RequestBody VehicleHealthSaveRequestDto vehicleHealthSaveRequestDto) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtTokenProvider.getSubject(token);

        vehicleHealthService.saveVehicleHealth(userId, vehicleHealthSaveRequestDto);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
