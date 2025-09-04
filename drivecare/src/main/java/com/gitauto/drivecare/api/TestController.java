package com.gitauto.drivecare.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/hello")
    public ResponseEntity<TestDto> getHelloInfo(@RequestBody TestDto testDto) {
        return ResponseEntity.ok(testDto);
    }
}
