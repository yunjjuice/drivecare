package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.dealer.dto.DealerProfileDto;
import com.gitauto.drivecare.dealer.entity.CarCenterEntity;
import com.gitauto.drivecare.dealer.service.DealerProfileService;
import com.gitauto.drivecare.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SessionAttributes("loginUser")
@RequiredArgsConstructor
public class DealerProfileRestController {

    private final DealerProfileService service;

    @PostMapping("/api/dealer/profile")
    public ResponseEntity<?> updateCarCenterInfo(@RequestBody DealerProfileDto dto, @SessionAttribute("loginUser") UserEntity loginUser) {
        try {
            CarCenterEntity savedEntity = service.updateCarCenterInfo(dto);
            service.updateUserCarCenterInfo(loginUser.getId(), savedEntity.getId());
            if (savedEntity == null) {
                return ResponseEntity.status(500).body("저장에 실패했습니다.");
            }
            return ResponseEntity.ok(savedEntity);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("저장 중 오류");
        }
    }
}
