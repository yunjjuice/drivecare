package com.gitauto.drivecare.owner.controller;

import com.gitauto.drivecare.owner.dto.CarCenterResponseDto;
import com.gitauto.drivecare.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest-api/owner")
public class OwnerRestController {

    private final OwnerService ownerService;

    @GetMapping("/car-center/list")
    public ResponseEntity<List<CarCenterResponseDto>> getCarCenterList(){
        return ResponseEntity.ok().body(ownerService.getCarCenterList());
    }
}
