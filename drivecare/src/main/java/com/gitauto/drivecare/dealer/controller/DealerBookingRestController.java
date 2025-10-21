package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.dealer.dto.ReservationDto;
import com.gitauto.drivecare.dealer.service.DealerBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@SessionAttributes("loginUser")
@RequiredArgsConstructor
public class DealerBookingRestController {

    private final DealerBookingService service;

    @PutMapping("/api/dealer/booking/{reservationId}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long reservationId, @RequestBody Map<String, String> requestBody) {
        try {
            String approveStatusStr = requestBody.get("approveStatus");

            if (approveStatusStr == null || approveStatusStr.length() != 1) {
                return ResponseEntity.badRequest().body("잘못된 승인 상태 값입니다.");
            }

            ReservationDto reservation = service.updateAppoveStatus(reservationId, approveStatusStr.charAt(0));
            if (reservation == null) {
                return ResponseEntity.status(500).body("업데이트에 실패했습니다.");
            }
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("업데이트 중 오류");
        }
    }
}