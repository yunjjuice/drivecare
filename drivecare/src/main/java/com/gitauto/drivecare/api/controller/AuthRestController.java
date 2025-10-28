package com.gitauto.drivecare.api.controller;

import com.gitauto.drivecare.api.dto.ApiResponse;
import com.gitauto.drivecare.api.service.AuthService;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody UserInfoEntity userInfo) {
        return ResponseEntity.ok(ApiResponse.ok(authService.login(userInfo.getUserId(), userInfo.getPassword())));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(ApiResponse.ok(authService.refresh(request.get("refreshToken").toString())));
    }
}
