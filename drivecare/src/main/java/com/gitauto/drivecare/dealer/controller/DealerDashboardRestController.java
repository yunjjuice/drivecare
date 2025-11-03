package com.gitauto.drivecare.dealer.controller;

import com.gitauto.drivecare.dealer.service.DealerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DealerDashboardRestController {

    private final DealerDashboardService dealerDashboardService;

    @PatchMapping("/repair/{id}/status")
    public Map<String, Character> updateRepairStatus(@PathVariable Long id) {
        Character newSatus = dealerDashboardService.updateRepairStatus(id);
        return Map.of("newStatus", newSatus);
    }
}
