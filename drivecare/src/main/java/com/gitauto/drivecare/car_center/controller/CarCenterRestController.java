package com.gitauto.drivecare.car_center.controller;

import com.gitauto.drivecare.car_center.dto.CarCenterResponseDto;
import com.gitauto.drivecare.car_center.service.CarCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ajax/carcenter")
public class CarCenterRestController {

    private final CarCenterService carCenterService;

    @GetMapping("/list")
    public ResponseEntity<List<CarCenterResponseDto>> getCarCenterList(){
        return ResponseEntity.ok().body(carCenterService.getCarCenterList());
    }
}
