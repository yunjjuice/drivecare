package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.dto.ApiResponse;
import com.gitauto.drivecare.api.service.DrivingDataService;
import com.gitauto.drivecare.database.user_driving_stat.entity.UserDrivingStatEntity;
import com.gitauto.drivecare.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driving")
@RequiredArgsConstructor
public class DrivingDataRestController {

    private final DrivingDataService drivingDataService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveDrivingData(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody UserDrivingStatEntity drivingData) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtTokenProvider.getSubject(token);

        return ResponseEntity.ok(ApiResponse.ok(drivingDataService.saveDrivingData(userId, drivingData)));
    }
}
